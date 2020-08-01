package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
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
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

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
    private final Disposable.Swap loadTodoByIdDisposableContainer;

    private TodoContainer todoContainer;

    public TodoListView(TodoService todoService) {
        this.todoService = todoService;
        loadTodoByIdDisposableContainer = Disposables.swap();
        initGui();
    }

    private void initGui() {
        CreateTodoPanel createTodoLayout = new CreateTodoPanel();
        add(createTodoLayout);

        setAlignItems(Alignment.CENTER);
    }

    private void accessUIAndDrawTodo(Todo todo) {
        log.info("Drawing todo with id {}", todo.getId());
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

    private TodoService getTodoService() {
        return todoService;
    }

    private void showDialogWithMessage(String message) {
        Dialog dialog = new Dialog();
        dialog.add(new Label(message));
        dialog.open();
    }
}
