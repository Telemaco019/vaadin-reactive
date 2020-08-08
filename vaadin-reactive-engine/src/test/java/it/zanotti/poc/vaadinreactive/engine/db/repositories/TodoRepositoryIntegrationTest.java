package it.zanotti.poc.vaadinreactive.engine.db.repositories;

import it.zanotti.poc.vaadinreactive.engine.VaadinReactiveEngineApplication;
import it.zanotti.poc.vaadinreactive.engine.db.dto.TodoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

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
    public void findAll() {
        LocalDateTime date = LocalDateTime.of(2020, 8, 19, 0, 0);
        final TodoDto firstExpected = new TodoDto(1, date, "first");
        final TodoDto secondExpected = new TodoDto(2, date, "second");
        final TodoDto thirdExpected = new TodoDto(3, date, "third");

        todoRepository.findAll()
                .as(StepVerifier::create)
                .expectNext(firstExpected)
                .expectNext(secondExpected)
                .expectNext(thirdExpected)
                .verifyComplete();
    }
}
