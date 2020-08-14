package it.zanotti.poc.vaadinreactive.portal.transformers;

import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.portal.model.SubmitTextEvent;
import it.zanotti.poc.vaadinreactive.portal.model.TodoUIModel;
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
public class TodoCreationTransformer implements Transformer<SubmitTextEvent, TodoUIModel> {
    private final TodoService todoService;

    @Autowired
    public TodoCreationTransformer(TodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    public Publisher<TodoUIModel> apply(Flux<SubmitTextEvent> submitTextEventFlux) {
        return submitTextEventFlux.map(UIEvent::getContent)
                .map(Todo::create)
                .flatMap(todo -> todoService.saveOrUpdateTodo(todo)
                        .flux()
                        .map(TodoUIModel::success)
                        .startWith(TodoUIModel.inProgress())
                        .onErrorResume(e -> Flux.just(TodoUIModel.failure(e)))
                );
    }
}
