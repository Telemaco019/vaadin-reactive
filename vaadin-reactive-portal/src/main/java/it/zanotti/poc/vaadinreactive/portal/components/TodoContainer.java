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
    private List<Todo> drawnTodos;
    private Label titleLabel;

    public TodoContainer() {
        drawnTodos = Lists.newArrayList();
        initGui();
    }

    public void initGui() {
        removeAll();

        titleLabel = new Label("Loaded Todos");
        add(titleLabel);

        setSizeFull();
        refreshTitleLabelVisibility();
    }

    public void addTodo(Todo todo) {
        drawnTodos.add(todo);
        add(createTodoCard(todo));
        refreshTitleLabelVisibility();
    }

    private VerticalLayout createTodoCard(Todo todo) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(Boolean.TRUE);

        card.add(new Label(String.format("Created on %s", todo.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
        card.add(new Label(todo.getDescription()));

        return card;
    }

    private void refreshTitleLabelVisibility() {
        titleLabel.setVisible(!drawnTodos.isEmpty());
    }
}
