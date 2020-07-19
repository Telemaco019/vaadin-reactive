package it.zanotti.poc.vaadinreactive.core.model;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Data
public class Todo {
    private Integer id;
    private LocalDate creationDate;
    private String description;
}
