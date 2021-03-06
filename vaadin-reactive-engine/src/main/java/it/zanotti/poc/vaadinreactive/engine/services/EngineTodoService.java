package it.zanotti.poc.vaadinreactive.engine.services;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.engine.converters.TodoConverter;
import it.zanotti.poc.vaadinreactive.engine.db.dto.TodoDto;
import it.zanotti.poc.vaadinreactive.engine.db.repositories.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Slf4j
@Service
public class EngineTodoService implements TodoService {
    private final TodoConverter converter = new TodoConverter();
    private final TodoRepository todoRepository;

    @Autowired
    public EngineTodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public Flux<Todo> getTodos() {
        return todoRepository.findAll()
                .subscribeOn(Schedulers.elastic())
                .doOnNext(todoDto -> log.debug("Loaded {}", todoDto))
                .doOnError(e -> log.error("Error loading todos: {}", e.getMessage(), e))
                .map(converter::convertFromDb)
                .map(this::performHeavyComputation)
                .doOnError(e -> log.error("Error converting dto to model: {}", e.getMessage(), e))
                .doOnNext(todo -> log.debug("Loaded {}", todo))
                .doOnComplete(() -> log.debug("All todos have been loaded"));
    }

    private Todo performHeavyComputation(Todo todo) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage(), e);
        }

        return todo;
    }

    @Override
    public Mono<Todo> getTodoById(Integer todoId) {
        return todoRepository.findById(todoId)
                .subscribeOn(Schedulers.elastic())
                .map(converter::convertFromDb)
                .doOnSuccess(todo -> log.debug("Loaded todo with id {}", todo.getId()))
                .doOnError(e -> log.error("Error loading todo with id {}: {}", todoId, e.getMessage(), e));
    }

    @Override
    public Mono<Todo> saveOrUpdateTodo(Todo todo) {
        TodoDto todoDto = converter.convertToDb(todo);
        return todoRepository.save(todoDto)
                .subscribeOn(Schedulers.elastic())
                .doOnError(e -> log.error("Error saving todo: {}", e.getMessage(), e))
                .map(converter::convertFromDb)
                .doOnError(e -> log.error("Error converting dto to model: {}", e.getMessage()));
    }

    @Override
    public Mono<Integer> deleteTodoById(Integer todoId) {
        /*
        Mono<Void> is always empty, since Void cannot be instantiated. A Mono<Void> it is thus just like a RxJava Completable,
        that either completes without returning any value or fails. In order to make it return just one item (e.g. the
        id of the entity that has been deleted), we use .map(void->int) for casting the Mono to a Mono<Integer>
        and .swithIfEmpty() for mapping the "complete" event to an integer emission.
         */
        return todoRepository.deleteById(todoId)
                .map(aVoid -> todoId) // used just for casting, it will actually never be called
                .switchIfEmpty(Mono.just(todoId))
                .subscribeOn(Schedulers.elastic())
                .doOnSuccess(aVoid -> log.debug("Deleted todo with id {}", todoId))
                .doOnError(e -> log.error("Error deleting todo with id {}: {}", todoId, e.getMessage(), e));
    }
}
