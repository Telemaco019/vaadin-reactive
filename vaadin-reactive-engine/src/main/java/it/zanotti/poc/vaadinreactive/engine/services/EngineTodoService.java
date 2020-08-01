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
    public Flux<Todo> getTodos() {
        Stream<Todo> todosStream = getAllTodos();
        return Flux.fromStream(todosStream).delayElements(Duration.ofSeconds(2));
    }

    private Stream<Todo> getAllTodos() {
        return todoDao.getTodos().stream().map(converter::convertFromDb);
    }

    @Override
    public Mono<Todo> getTodoById(Integer todoId) {
        return fetchTodoById(todoId)
                .map(Mono::just)
                .orElse(Mono.empty());
    }

    private Optional<Todo> fetchTodoById(Integer todoId) {
        return todoDao.getTodoById(todoId).map(converter::convertFromDb);
    }

    private void performSomeHeavyComputation() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage(), e);
        }
    }
}
