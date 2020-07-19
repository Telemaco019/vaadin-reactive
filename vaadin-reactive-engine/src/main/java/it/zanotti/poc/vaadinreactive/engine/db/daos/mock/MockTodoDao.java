package it.zanotti.poc.vaadinreactive.engine.db.daos.mock;

import com.google.common.collect.Maps;
import it.zanotti.poc.vaadinreactive.engine.db.daos.TodoDao;
import it.zanotti.poc.vaadinreactive.engine.db.dto.TodoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Slf4j
@Component
public class MockTodoDao implements TodoDao {
    private static final String LORE_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    private final Map<Integer, TodoDto> todos = Maps.newHashMap();

    public MockTodoDao() {
        populateTodosMap();
    }

    private void populateTodosMap() {
        TodoDto todo_1 = new TodoDto();
        todo_1.setId(1);
        todo_1.setDescription(LORE_IPSUM);
        todo_1.setCreationDate(LocalDate.now());

        TodoDto todo_2 = new TodoDto();
        todo_2.setId(2);
        todo_2.setDescription(LORE_IPSUM);
        todo_2.setCreationDate(LocalDate.of(2020, 1, 1));

        TodoDto todo_3 = new TodoDto();
        todo_3.setId(3);
        todo_3.setDescription(LORE_IPSUM);
        todo_3.setCreationDate(LocalDate.of(2019, 2, 22));

        TodoDto todo_4 = new TodoDto();
        todo_4.setId(4);
        todo_4.setDescription(LORE_IPSUM);
        todo_4.setCreationDate(LocalDate.of(2015, 5, 18));

        todos.put(todo_1.getId(), todo_1);
        todos.put(todo_2.getId(), todo_2);
        todos.put(todo_3.getId(), todo_3);
        todos.put(todo_4.getId(), todo_4);
    }

    @Override
    public List<TodoDto> getTodos() {
        return new ArrayList<>(todos.values());
    }

    @Override
    public Optional<TodoDto> getTodoById(Integer id) {
        performSomeHeavyComputation();
        return Optional.ofNullable(todos.get(id));
    }

    private void performSomeHeavyComputation() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("{}", e.getMessage(), e);
        }
    }
}
