package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import it.zanotti.poc.vaadinreactive.portal.components.CreateTodoPanel;
import it.zanotti.poc.vaadinreactive.portal.components.TodoContainer;
import it.zanotti.poc.vaadinreactive.portal.model.Action;
import it.zanotti.poc.vaadinreactive.portal.transformers.TodoCreationEventTransformer;
import it.zanotti.poc.vaadinreactive.portal.transformers.TodoDeletionTransformer;
import it.zanotti.poc.vaadinreactive.portal.utils.UIUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.Disposables;

/**
 * @author Michele Zanotti on 19/07/20
 **/
@Slf4j
@RouteAlias(value = StringUtils.EMPTY, layout = MainLayout.class)
@Route(value = AppConstants.ROUTE_NAME_TODO_LIST, layout = MainLayout.class)
@Component
@UIScope
public class TodoListView extends VerticalLayout {
    private final TodoService todoService;
    private final TodoCreationEventTransformer todoCreationEventTransformer;
    private final TodoDeletionTransformer todoDeletionTransformer;
    private final Disposable.Swap loadTodosDisposable;

    private TodoContainer todoContainer;
    private HorizontalLayout loadingInfoLayout;
    private CreateTodoPanel createTodoPanel;

    @Autowired
    public TodoListView(TodoService todoService,
                        TodoCreationEventTransformer todoCreationEventTransformer,
                        TodoDeletionTransformer todoDeletionTransformer) {
        this.todoService = todoService;
        this.todoCreationEventTransformer = todoCreationEventTransformer;
        this.todoDeletionTransformer = todoDeletionTransformer;
        this.loadTodosDisposable = Disposables.swap();
        initGui();
    }

    private void initGui() {
        createTodoPanel = new CreateTodoPanel();
        add(createTodoPanel);

        loadingInfoLayout = createLoadingInfoLayout();
        add(loadingInfoLayout);

        todoContainer = new TodoContainer(todoDeletionTransformer);
        add(todoContainer);

        createTodoPanel.getOnTodoCreateFlux()
                .transform(todoCreationEventTransformer)
                .subscribe(this::onTodoCreated);


        setAlignItems(Alignment.CENTER);
    }

    private void onTodoCreated(Action<Todo> action) {
        if (action.isInProgress()) {
            accessUIAndExecuteAction(() -> createTodoPanel.clearTextfield());
        } else {
            if (action.isSuccess()) {
                accessUIAndDrawTodo(action.getModel());
            } else {
                Throwable e = action.getException();
                log.error("{}", e.getMessage(), e);
                accessUIAndShowErrorDialog(e);
            }
        }
    }

    private HorizontalLayout createLoadingInfoLayout() {
        HorizontalLayout result = new HorizontalLayout();

        var loadingIndicator = new Label("Loading todos...");
        result.add(loadingIndicator);

        var btnCancelLoading = new Button("Cancel loading", e -> loadTodosDisposable.dispose());
        btnCancelLoading.getStyle().set("cursor", "pointer");
        btnCancelLoading.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        result.add(btnCancelLoading);

        result.setAlignItems(Alignment.CENTER);
        result.setAlignSelf(Alignment.CENTER);
        return result;
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadTodos();
        showLoadingInfo();
    }

    private void loadTodos() {
        Disposable subscription = todoService.getTodos().subscribe(
                this::accessUIAndDrawTodo,
                this::accessUIAndShowErrorDialog,
                this::accessUIAndHideLoadingInfo
        );
        loadTodosDisposable.update(subscription); // dispose the previous Disposable and update the container with the new one
    }

    private void showLoadingInfo() {
        loadingInfoLayout.setVisible(Boolean.TRUE);
    }

    private void accessUIAndDrawTodo(Todo todo) {
        log.debug("Drawing todo with id {}", todo.getId());
        accessUIAndExecuteAction(() -> getTodoContainer().addTodo(todo));
    }

    private void accessUIAndShowErrorDialog(Throwable throwable) {
        accessUIAndExecuteAction(() -> UIUtils.showDialogWithMessage(throwable.getMessage()));
    }

    private void accessUIAndHideLoadingInfo() {
        accessUIAndExecuteAction(() -> loadingInfoLayout.setVisible(Boolean.FALSE));
    }

    private void accessUIAndExecuteAction(Runnable action) {
        getUI().ifPresent(ui -> ui.access(action::run));
    }

    private TodoContainer getTodoContainer() {
        return todoContainer;
    }
}
