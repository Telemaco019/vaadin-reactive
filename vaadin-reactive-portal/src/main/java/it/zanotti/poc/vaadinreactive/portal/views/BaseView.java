package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.portal.components.TodoContainer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Consumer;

/**
 * @author Michele Zanotti on 19/07/20
 **/
@Push
@Slf4j
public abstract class BaseView extends VerticalLayout {
    private final TodoService todoService;
    private TodoContainer todoContainer;

    public BaseView(TodoService todoService) {
        this.todoService = todoService;
        initGui();
    }

    private void initGui() {
        Button loadTodosButton = new Button("Load all Todos", e -> loadAndDisplayTodos());
        add(loadTodosButton);

        HorizontalLayout loadSingleTodoLayout = createLoadSingleTodoLayout();
        add(new Label("Load single Todo by ID"));
        add(loadSingleTodoLayout);

        todoContainer = new TodoContainer();
        add(todoContainer);

        setAlignItems(Alignment.CENTER);
    }

    private void loadAndDisplayTodos() {
        todoContainer.initGui();
        loadAllTodos(this::accessUIAndDrawTodo);
    }

    private HorizontalLayout createLoadSingleTodoLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.CENTER);

        Button loadTodoButton = new Button("Load");
        loadTodoButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        loadTodoButton.setEnabled(Boolean.FALSE);

        TextField todoIdTextfield = new TextField("Insert todo ID");
        todoIdTextfield.setValueChangeMode(ValueChangeMode.EAGER);

        todoIdTextfield.addValueChangeListener(e -> loadTodoButton.setEnabled(StringUtils.isNotEmpty(e.getValue())));
        loadTodoButton.addClickListener(e -> loadAndDisplayTodo(Integer.valueOf(todoIdTextfield.getValue())));

        layout.add(todoIdTextfield);
        layout.add(loadTodoButton);

        return layout;
    }

    private void loadAndDisplayTodo(Integer todoId) {
        todoContainer.initGui();
        loadTodoById(todoId, this::accessUIAndDrawTodo);
    }

    private void accessUIAndDrawTodo(Todo todo) {
        log.info("Drawing todo with id {}", todo.getId());
        getUI().ifPresent(ui -> ui.access(() -> todoContainer.addTodo(todo)));
    }

    protected abstract void loadAllTodos(Consumer<Todo> onTodoLoadedCallback);

    protected abstract void loadTodoById(Integer id, Consumer<Todo> onTodoLoadedCallback);

    protected TodoService getTodoService() {
        return todoService;
    }
}
