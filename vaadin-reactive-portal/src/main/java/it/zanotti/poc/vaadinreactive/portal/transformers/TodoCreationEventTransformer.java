package it.zanotti.poc.vaadinreactive.portal.transformers;

import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.portal.model.EventProgress;
import it.zanotti.poc.vaadinreactive.portal.model.UIEvent;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Michele Zanotti on 14/08/20
 **/
@UIScope
@Component
public class TodoCreationEventTransformer implements Transformer<UIEvent<String>, EventProgress<Todo>> {
    private final TodoService todoService;

    @Autowired
    public TodoCreationEventTransformer(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public Publisher<EventProgress<Todo>> apply(Flux<UIEvent<String>> stringContentEventFlux) {
        return stringContentEventFlux.map(UIEvent::getContent)
                .map(Todo::create)
                .flatMap(todo -> todoService.saveOrUpdateTodo(todo)
                        .flux()
                        .map(EventProgress::success)
                        .startWith(EventProgress.inProgress())
                        .onErrorResume(e -> Flux.just(EventProgress.failure(e)))
                );
    }
}
