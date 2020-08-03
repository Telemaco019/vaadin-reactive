package it.zanotti.poc.vaadinreactive.core.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Data
@NoArgsConstructor
public class Todo {
    private Integer id;
    private LocalDate creationDate;
    private String description;

    public Todo(String description, LocalDate creationDate) {
        this.description = description;
        this.creationDate = creationDate;
    }

    public static Todo create(String description) {
        return new Todo(description, LocalDate.now());
    }
}
