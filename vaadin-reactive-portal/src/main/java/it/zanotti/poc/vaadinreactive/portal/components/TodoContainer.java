package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * @author Michele Zanotti on 19/07/20
 **/
public class TodoContainer extends VerticalLayout {
    private final List<TodoItem> containedTodoItems;

    public TodoContainer() {
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
        add(todoItem);
    }
}
