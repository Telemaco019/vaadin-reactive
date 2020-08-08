package it.zanotti.poc.vaadinreactive.engine.db.repositories;

import it.zanotti.poc.vaadinreactive.engine.VaadinReactiveEngineApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Michele Zanotti on 02/08/20
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {VaadinReactiveEngineApplication.class})
@ActiveProfiles("test")
public class TodoRepositoryIntegrationTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test() {
        todoRepository.findAll().subscribe(todo -> todo.toString());
    }
}
