package it.zanotti.poc.vaadinreactive.engine.services;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.engine.converters.TodoConverter;
import it.zanotti.poc.vaadinreactive.engine.db.daos.TodoDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Slf4j
@Service
public class EngineTodoService implements TodoService {
    private final TodoConverter converter = new TodoConverter();
    private final TodoDao todoDao;

    @Autowired
    public EngineTodoService(TodoDao todoDao) {
        this.todoDao = todoDao;
    }

    @Override
    public Flux<Todo> getTodosFlux() {
        Stream<Todo> todosStream = getAllTodos();
        return Flux.fromStream(todosStream).delayElements(Duration.ofSeconds(2));
    }

    @Override
    public Stream<Todo> getTodosStream() {
        return getAllTodos();
    }

    private Stream<Todo> getAllTodos() {
        return todoDao.getTodos().stream().map(converter::convertFromDb);
    }

    @Override
    public Mono<Todo> getTodoMonoById(Integer todoId) {
        return getTodoById(todoId)
                .map(Mono::just)
                .orElse(Mono.empty());
    }

    @Override
    public Optional<Todo> getTodoOptionalById(Integer todoId) {
        return getTodoById(todoId);
    }

    private Optional<Todo> getTodoById(Integer todoId) {
        return todoDao.getTodoById(todoId).map(converter::convertFromDb);
    }
}
