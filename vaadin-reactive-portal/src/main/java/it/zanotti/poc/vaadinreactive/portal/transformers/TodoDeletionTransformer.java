package it.zanotti.poc.vaadinreactive.portal.transformers;

import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.portal.model.Action;
import it.zanotti.poc.vaadinreactive.portal.model.UIEvent;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Michele Zanotti on 23/08/20
 **/
@UIScope
@Component
public class TodoDeletionTransformer implements Transformer<UIEvent<Integer>, Action<Integer>> {
    private final TodoService todoService;

    public TodoDeletionTransformer(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public Publisher<Action<Integer>> apply(Flux<UIEvent<Integer>> uiEventFlux) {
        return uiEventFlux.map(UIEvent::getContent)
                .flatMap(todoId -> todoService.deleteTodoById(todoId)
                        .flux()
                        .map(Action::success)
                        .startWith(Action.inProgress())
                        .onErrorResume(e -> Flux.just(Action.failure(e)))
                );
    }
}
