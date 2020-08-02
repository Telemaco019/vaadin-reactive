package it.zanotti.poc.vaadinreactive.engine.db.repositories;

import it.zanotti.poc.vaadinreactive.engine.VaadinReactiveEngineApplication;
import it.zanotti.poc.vaadinreactive.engine.config.AppConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

/**
 * @author Michele Zanotti on 02/08/20
 **/
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class, VaadinReactiveEngineApplication.class})
public class TodoRepositoryIntegrationTest {
    @Autowired
    private TodoRepository todoRepository;

    @Test
    public void test() {
        todoRepository.findAll().as(StepVerifier::create).verifyComplete();
    }
}
