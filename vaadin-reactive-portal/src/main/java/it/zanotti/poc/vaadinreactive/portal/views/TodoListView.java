package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import it.zanotti.poc.vaadinreactive.portal.components.CreateTodoPanel;
import it.zanotti.poc.vaadinreactive.portal.components.TodoContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    private final Disposable.Swap loadTodosDisposable;

    private TodoContainer todoContainer;

    public TodoListView(TodoService todoService) {
        this.todoService = todoService;
        this.loadTodosDisposable = Disposables.swap();
        initGui();
    }

    private void initGui() {
        CreateTodoPanel createTodoLayout = new CreateTodoPanel();
        add(createTodoLayout);

        todoContainer = new TodoContainer();
        add(todoContainer);

        createTodoLayout.getOnTodoCreateFlux()
                .map(Todo::create)
                .flatMap(todoService::saveOrUpdateTodo)
                .doOnError(e -> log.error("{}", e.getMessage(), e))
                .subscribe(this::accessUIAndDrawTodo, this::accessUIAndShowErrorDialog);

        setAlignItems(Alignment.CENTER);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        Disposable subscription = todoService.getTodos()
                .subscribe(this::accessUIAndDrawTodo, this::accessUIAndShowErrorDialog);
        loadTodosDisposable.replace(subscription);
    }

    private void accessUIAndDrawTodo(Todo todo) {
        log.debug("Drawing todo with id {}", todo.getId());
        accessUIAndExecuteAction(() -> getTodoContainer().addTodo(todo));
    }

    private void accessUIAndShowErrorDialog(Throwable throwable) {
        accessUIAndExecuteAction(() -> showDialogWithMessage(throwable.getMessage()));
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
