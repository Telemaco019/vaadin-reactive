package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.portal.utils.UIUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author Michele Zanotti on 19/07/20
 **/
@Slf4j
public class TodoContainer extends VerticalLayout {
    private final List<TodoItem> containedTodoItems;
    private final TodoService todoService;

    public TodoContainer(TodoService todoService) {
        this.todoService = todoService;
        containedTodoItems = Lists.newArrayList();
        initGui();
    }

    private void initGui() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    public void addTodo(Todo todo) {
        TodoItem todoItem = new TodoItem();
        todoItem.setModel(todo);
        containedTodoItems.add(todoItem);

        todoItem.getOnTodoDeleteFlux()
                .flatMap(event -> todoService.deleteTodoById(event.getContent()))
                .subscribe(
                        todoId -> accessUIAndExecuteAction(() -> removeTodoItem(todoId)),
                        this::accessUIAndShowErrorDialog
                );

        add(todoItem);
    }

    private void removeTodoItem(Integer todoId) {
        containedTodoItems.stream()
                .filter(todoItem -> todoItem.getModel().getId().equals(todoId))
                .forEach(this::remove);
    }

    private void accessUIAndShowErrorDialog(Throwable throwable) {
        accessUIAndExecuteAction(() -> UIUtils.showDialogWithMessage(throwable.getMessage()));
    }

    private void accessUIAndExecuteAction(Runnable action) {
        getUI().ifPresent(ui -> ui.access(action::run));
    }
}
