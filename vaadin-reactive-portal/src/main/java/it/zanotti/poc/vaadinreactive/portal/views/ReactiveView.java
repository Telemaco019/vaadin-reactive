package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.Disposables;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * @author Michele Zanotti on 12/07/20
 **/
@RouteAlias(value = StringUtils.EMPTY, layout = MainLayout.class)
@Route(value = AppConstants.ROUTE_NAME_REACTIVE, layout = MainLayout.class)
@Component
@UIScope
public class ReactiveView extends BaseView {

    private final Disposable.Swap loadAllTodosDisposableContainer;
    private final Disposable.Swap loadTodoByIdDisposableContainer;

    public ReactiveView(TodoService todoService) {
        super(todoService);
        loadAllTodosDisposableContainer = Disposables.swap();
        loadTodoByIdDisposableContainer = Disposables.swap();
    }

    @Override
    protected void loadAllTodos(Consumer<Todo> onTodoLoadedCallback, Runnable onAllTodosLoadedCallback) {
        Disposable subscription = getTodoService().getTodosFlux().subscribe(
                onTodoLoadedCallback,
                this::accessUIAndShowErrorDialog,
                onAllTodosLoadedCallback
        );

        // Dispose the previous subscription (if any) and replace it with the new one.
        // In this way if the users request to load the Todos a second time before the previous requested flux is over,
        // then the previous flux is disposed before starting the new one.
        loadAllTodosDisposableContainer.update(subscription);
    }

    @Override
    protected void loadTodoById(Integer id, Consumer<Todo> onTodoLoadedCallback) {
        Disposable subscription = getTodoService().getTodoMonoById(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException(String.format("Todo with id %d not found", id))))
                .subscribe(
                        onTodoLoadedCallback,
                        this::accessUIAndShowErrorDialog
                );

        loadTodoByIdDisposableContainer.update(subscription);
    }
}
