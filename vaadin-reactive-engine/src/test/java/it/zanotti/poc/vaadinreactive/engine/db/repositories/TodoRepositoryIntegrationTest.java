package it.zanotti.poc.vaadinreactive.engine.db.repositories;

import it.zanotti.poc.vaadinreactive.engine.VaadinReactiveEngineApplication;
import it.zanotti.poc.vaadinreactive.engine.config.R2dbcConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Michele Zanotti on 02/08/20
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {VaadinReactiveEngineApplication.class, R2dbcConfig.class})
public class TodoRepositoryIntegrationTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test() {
//        todoRepository.findAll().as(StepVerifier::create).verifyComplete();
        todoRepository.findAll().subscribe(todo -> {});
    }
}
