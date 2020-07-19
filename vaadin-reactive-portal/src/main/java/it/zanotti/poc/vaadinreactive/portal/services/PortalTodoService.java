package it.zanotti.poc.vaadinreactive.portal.services;

import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Slf4j
@UIScope
@Service
public class PortalTodoService implements TodoService {
    private final WebClient webClient;

    public PortalTodoService() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/api")
                .build();
    }

    @Override
    public Flux<Todo> getTodosFlux() {
        return webClient.get()
                .uri("/reactive/todos")
                .retrieve()
                .bodyToFlux(Todo.class)
                .log();
    }

    @Override
    public Stream<Todo> getTodosStream() {
        return webClient.get()
                .uri("/blocking/todos")
                .retrieve()
                .bodyToFlux(Todo.class)
                .toStream();
    }

    @Override
    public Mono<Todo> getTodoMonoById(Integer todoId) {
        return webClient.get()
                .uri("/reactive/todos/{id}", todoId)
                .retrieve()
                .bodyToMono(Todo.class);
    }

    @Override
    public Optional<Todo> getTodoOptionalById(Integer todoId) {
        return webClient.get()
                .uri("/blocking/todos/{id}", todoId)
                .retrieve()
                .bodyToMono(Todo.class)
                .blockOptional();
    }
}
