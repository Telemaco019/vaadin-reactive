package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

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
    protected void loadAllTodos(Consumer<Todo> onTodoLoadedCallback) {

    }

    @Override
    protected void loadTodoById(Integer id, Consumer<Todo> onTodoLoadedCallback) {

    }
}
