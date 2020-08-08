package it.zanotti.poc.vaadinreactive.engine.db.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * @author Michele Zanotti on 18/07/20
 **/
@Data
@Table("TODO")
@NoArgsConstructor
@AllArgsConstructor
public class TodoDto {
    @Id
    @Column("ID")
    private Integer id;
    @Column("CREATION_DATE")
    private LocalDateTime creationDate;
    @Column("TEXT")
    private String text;
}
