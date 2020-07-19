package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Michele Zanotti on 12/07/20
 **/
@Component
@UIScope
@Route(value = AppConstants.ROUTE_NAME_BLOCKING, layout = MainLayout.class)
public class BlockingView extends BaseView {
    public BlockingView(TodoService todoService) {
        super(todoService);
    }

    @Override
    protected void onLoadAllTodosClicked() {
        getTodoService().getTodosStream().forEach(this::drawTodo);
    }

    @Override
    protected void onLoadTodoByIdClicked(Integer todoId) {
        Optional<Todo> todoOpt = getTodoService().getTodoOptionalById(todoId);
        if (todoOpt.isPresent()) {
            drawTodo(todoOpt.get());
        } else {
            showDialogWithMessage(String.format("Todo with id %d not found", todoId));
        }
    }

    private void drawTodo(Todo todo) {
        getTodoContainer().addTodo(todo);
    }
}
