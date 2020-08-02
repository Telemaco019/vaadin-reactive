package it.zanotti.poc.vaadinreactive.engine.db.repositories;

import it.zanotti.poc.vaadinreactive.engine.db.dto.TodoDto;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 * @author Michele Zanotti on 02/08/20
 **/
public interface TodoRepository extends ReactiveCrudRepository<TodoDto, Integer> {

    @Query("SELECT COUNT(*) FROM TODO")
    Mono<Integer> customQuery();
}
