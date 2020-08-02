package it.zanotti.poc.vaadinreactive.engine.converters;

import it.zanotti.poc.vaadinreactive.engine.db.dto.TodoDto;
import it.zanotti.poc.vaadinreactive.core.model.Todo;

/**
 * @author Michele Zanotti on 18/07/20
 **/
public class TodoConverter {
    public Todo convertFromDb(TodoDto todo) {
        Todo result = new Todo();

        result.setId(todo.getId());
        result.setCreationDate(todo.getCreationDate());
        result.setDescription(todo.getText());

        return result;
    }

    public TodoDto convertToDb(Todo todo) {
        TodoDto result = new TodoDto();

        result.setId(todo.getId());
        result.setCreationDate(todo.getCreationDate());
        result.setText(todo.getDescription());

        return result;
    }
}
