package it.zanotti.poc.vaadinreactive.engine.db.dto;

import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Data
@Table("TODO")
public class TodoDto {
    @Column("ID")
    private Integer id;
    @Column("CREATION_DATE")
    private LocalDate creationDate;
    @Column("TEXT")
    private String text;
}
