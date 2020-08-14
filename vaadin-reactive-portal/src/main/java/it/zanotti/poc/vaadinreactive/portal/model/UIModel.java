package it.zanotti.poc.vaadinreactive.portal.model;

import lombok.Data;

/**
 * @author Michele Zanotti on 14/08/20
 **/
@Data
public abstract class UIModel<T> {
    private final boolean inProgress;
    private final boolean success;
    private final Throwable exception;
    private final T model;

    protected UIModel(boolean inProgress, boolean success, T model, Throwable exception) {
        this.inProgress = inProgress;
        this.success = success;
        this.model = model;
        this.exception = exception;
    }
}
