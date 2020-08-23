package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.portal.model.Action;
import it.zanotti.poc.vaadinreactive.portal.transformers.TodoDeletionTransformer;
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
    private final TodoDeletionTransformer todoDeletionTransformer;

    public TodoContainer(TodoDeletionTransformer todoDeletionTransformer) {
        this.todoDeletionTransformer = todoDeletionTransformer;
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
                .transform(todoDeletionTransformer)
                .subscribe(this::manageTodoDeletion);

        add(todoItem);
    }

    private void manageTodoDeletion(Action<Integer> todoDeletionAction) {
        if (todoDeletionAction.isInError()) {
            accessUIAndShowErrorDialog(todoDeletionAction.getException());
        } else if (todoDeletionAction.isSuccess()) {
            accessUIAndExecuteAction(() -> removeTodoItem(todoDeletionAction.getModel()));
        }
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
