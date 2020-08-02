package it.zanotti.poc.vaadinreactive.engine.services;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.engine.converters.TodoConverter;
import it.zanotti.poc.vaadinreactive.engine.db.repositories.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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
                .map(converter::convertFromDb)
                .doOnNext(todo -> log.debug("Loaded todo {}", todo.toString()))
                .doOnComplete(() -> log.debug("All todos have been loaded"))
                .doOnError(e -> log.error("Error loading todos: {}", e.getMessage(), e));
    }

    @Override
    public Mono<Todo> getTodoById(Integer todoId) {
        return Mono.justOrEmpty(fetchTodoById(todoId))
                .doOnSuccess(todo -> log.debug("Loaded todo with id {}", todo.getId()))
                .doOnError(e -> log.error("Error loading todo with id {}: {}", todoId, e.getMessage(), e));
    }

    private Optional<Todo> fetchTodoById(Integer todoId) {
        return Optional.empty();
    }
}
