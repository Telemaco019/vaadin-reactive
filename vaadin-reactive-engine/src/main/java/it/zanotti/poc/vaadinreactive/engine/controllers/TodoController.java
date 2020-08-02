package it.zanotti.poc.vaadinreactive.engine.controllers;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        log.info("Received load todos request");
        return todoService.getTodos();
    }

    @GetMapping("/api/todos/{todoId}")
    public Mono<Todo> getTodoReactive(@PathVariable Integer todoId) {
        log.info("Received load todo by id {} request", todoId);
        return todoService.getTodoById(todoId);
    }

    @PostMapping("/api/todos")
    public Mono<Todo> saveOrUpdateTodo(@RequestBody Todo todo) {
        log.info("Received save or update todo request");
        return todoService.saveOrUpdateTodo(todo);
    }
}
