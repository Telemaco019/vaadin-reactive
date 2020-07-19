package it.zanotti.poc.vaadinreactive.engine.db.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Data
public class TodoDto {
    private Integer id;
    private LocalDate creationDate;
    private String description;
}
