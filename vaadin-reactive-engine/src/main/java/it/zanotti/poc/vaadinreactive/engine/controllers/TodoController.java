package it.zanotti.poc.vaadinreactive.engine.controllers;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Slf4j
@RestController
public class TodoController {
    private final TodoService todoService;

    @Autowired
    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(value = "/api/todos", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Todo> getTodosReactive() {
        log.info("Loading todo flux");
        return todoService.getTodos();
    }

    @GetMapping("/api/todos/{todoId}")
    public Mono<Todo> getTodoReactive(@PathVariable Integer todoId) {
        log.info("Loading todo Mono with ID {}", todoId);
        return todoService.getTodoById(todoId);
    }
}
