package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
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
import it.zanotti.poc.vaadinreactive.portal.model.SubmitTextEvent;
import it.zanotti.poc.vaadinreactive.portal.model.TodoUIModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Flux;

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
    private final Disposable.Swap loadTodosDisposable;

    private TodoContainer todoContainer;
    private HorizontalLayout loadingInfoLayout;
    private CreateTodoPanel createTodoPanel;

    public TodoListView(TodoService todoService) {
        this.todoService = todoService;
        this.loadTodosDisposable = Disposables.swap();
        initGui();
    }

    private void initGui() {
        createTodoPanel = new CreateTodoPanel();
        add(createTodoPanel);

        loadingInfoLayout = createLoadingInfoLayout();
        add(loadingInfoLayout);

        todoContainer = new TodoContainer();
        add(todoContainer);

        createTodoPanel.getOnTodoCreateFlux()
                .map(SubmitTextEvent::getContent)
                .map(Todo::create)
                .flatMap(todo -> todoService.saveOrUpdateTodo(todo)
                        .flux()
                        .map(TodoUIModel::success)
                        .startWith(TodoUIModel.inProgress())
                        .onErrorResume(e -> Flux.just(TodoUIModel.failure(e))))
                .doOnError(e -> log.error("{}", e.getMessage(), e))
                .subscribe(this::onTodoCreated);

        setAlignItems(Alignment.CENTER);
    }

    private void onTodoCreated(TodoUIModel uiModel) {
        if (uiModel.isInProgress()) {
            accessUIAndExecuteAction(() -> createTodoPanel.clearTextfield());
        } else {
            if (uiModel.isSuccess()) {
                accessUIAndDrawTodo(uiModel.getModel());
            } else {
                accessUIAndShowErrorDialog(uiModel.getException());
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
        accessUIAndExecuteAction(() -> showDialogWithMessage(throwable.getMessage()));
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

    private void showDialogWithMessage(String message) {
        Dialog dialog = new Dialog();
        dialog.add(new Label(message));
        dialog.open();
    }
}
