package it.zanotti.poc.vaadinreactive.core.services;

import it.zanotti.poc.vaadinreactive.core.model.Todo;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michele Zanotti on 19/07/20
 **/
public interface
TodoService {
    Stream<Todo> getTodosStream();

    Optional<Todo> getTodoById(Integer todoId);
}
