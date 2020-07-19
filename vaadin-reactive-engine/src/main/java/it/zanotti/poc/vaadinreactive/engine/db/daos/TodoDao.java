package it.zanotti.poc.vaadinreactive.engine.db.daos;

import it.zanotti.poc.vaadinreactive.engine.db.dto.TodoDto;

import java.util.List;
import java.util.Optional;

/**
 * @author Michele Zanotti on 18/07/20
 **/
public interface TodoDao {
    List<TodoDto> getTodos();

    Optional<TodoDto> getTodoById(Integer id);
}
