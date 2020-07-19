package it.zanotti.poc.vaadinreactive.engine.controllers;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

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

    @GetMapping("/api/todos")
    public Flux<Todo> getTodos() {
        return Flux.fromStream(todoService.getTodosStream());
    }

    @GetMapping("/api/todo/{todoId}")
    public Mono<Todo> getTodo(@PathVariable Integer todoId) {
        Optional<Todo> todo = todoService.getTodoById(todoId);
        return todo.map(Mono::just).orElse(Mono.empty());
    }
}
