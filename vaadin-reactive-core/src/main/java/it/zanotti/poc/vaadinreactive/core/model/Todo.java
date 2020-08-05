package it.zanotti.poc.vaadinreactive.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Data
@NoArgsConstructor
public class Todo {
    private Integer id;
    private LocalDateTime creationDate;
    private String description;

    public Todo(String description, LocalDateTime creationDate) {
        this.description = description;
        this.creationDate = creationDate;
    }

    public static Todo create(String description) {
        return new Todo(description, LocalDateTime.now());
    }
}
