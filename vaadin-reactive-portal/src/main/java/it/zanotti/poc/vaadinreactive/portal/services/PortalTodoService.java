package it.zanotti.poc.vaadinreactive.portal.services;

import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.model.Todo;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<Todo> getTodos() {
        return webClient.get()
                .uri("/todos")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .retrieve()
                .bodyToFlux(Todo.class);
    }

    @Override
    public Mono<Todo> getTodoById(Integer todoId) {
        return webClient.get()
                .uri("/todos/{id}", todoId)
                .retrieve()
                .bodyToMono(Todo.class);
    }

    @Override
    public Mono<Todo> saveOrUpdateTodo(Todo todo) {
        return webClient.post()
                .uri("/todos")
                .contentType(MediaType.APPLICATION_JSON) // content type of the body of the request
                .body(Mono.just(todo), Todo.class) // or, equivalently, it could be .bodyValue(todo)
                .retrieve()
                .bodyToMono(Todo.class);
    }

    @Override
    public Mono<Integer> deleteTodoById(Integer todoId) {
        return webClient.delete()
                .uri("/todos/{id}", todoId)
                .retrieve()
                .bodyToMono(Integer.class);
    }
}
