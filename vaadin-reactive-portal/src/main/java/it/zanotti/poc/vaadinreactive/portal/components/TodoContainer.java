package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import org.apache.commons.compress.utils.Lists;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Michele Zanotti on 19/07/20
 **/
public class TodoContainer extends VerticalLayout {
    private final List<Todo> drawnTodos;

    public TodoContainer() {
        drawnTodos = Lists.newArrayList();
        initGui();
    }

    private void initGui() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
    }

    public void addTodo(Todo todo) {
        drawnTodos.add(todo);
        add(createTodoCard(todo));
    }

    private VerticalLayout createTodoCard(Todo todo) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(Boolean.TRUE);
        card.setWidth("30%");

        card.add(new Label(String.format("Created on %s", todo.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
        card.add(new Label(todo.getDescription()));

        return card;
    }
}
