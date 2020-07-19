package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

/**
 * @author Michele Zanotti on 12/07/20
 **/
@RouteAlias(value = StringUtils.EMPTY, layout = MainLayout.class)
@Route(value = AppConstants.ROUTE_NAME_REACTIVE, layout = MainLayout.class)
@Component
@UIScope
public class ReactiveView extends BaseView {
    public ReactiveView(TodoService todoService) {
        super(todoService);
    }

    @Override
    protected void loadAllTodos(Consumer<Todo> onTodoLoadedCallback) {
        getTodoService().getTodosFlux().subscribe(onTodoLoadedCallback);
    }

    @Override
    protected void loadTodoById(Integer id, Consumer<Todo> onTodoLoadedCallback) {

    }
}
