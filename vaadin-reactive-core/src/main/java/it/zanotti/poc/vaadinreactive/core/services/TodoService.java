package it.zanotti.poc.vaadinreactive.core.services;

import it.zanotti.poc.vaadinreactive.core.model.Todo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michele Zanotti on 19/07/20
 **/
public interface TodoService {
    Flux<Todo> getTodos();

    Mono<Todo> getTodoById(Integer todoId);
}
