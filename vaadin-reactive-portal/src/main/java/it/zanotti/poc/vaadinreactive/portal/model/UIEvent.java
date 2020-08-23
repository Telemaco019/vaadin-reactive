package it.zanotti.poc.vaadinreactive.portal.model;

import lombok.Data;

/**
 * @author Michele Zanotti on 14/08/20
 **/
@Data
public class UIEvent<T> {
    private final T content;

    public UIEvent(T content) {
        this.content = content;
    }
}
