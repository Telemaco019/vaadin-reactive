package it.zanotti.poc.vaadinreactive.portal.model;

import lombok.Data;

import java.util.Objects;

/**
 * @author Michele Zanotti on 14/08/20
 **/
@Data
public class Action<T> {
    private final boolean inProgress;
    private final boolean success;
    private final Throwable exception;
    private final T model;

    private Action(boolean inProgress, boolean success, T model, Throwable exception) {
        this.inProgress = inProgress;
        this.success = success;
        this.model = model;
        this.exception = exception;
    }

    public boolean isInError() {
        return !Objects.isNull(exception);
    }

    public static <T> Action<T> success(T model) {
        return new Action<>(false, true, model, null);
    }

    public static <T> Action<T> failure(Throwable exception) {
        return new Action<>(false, false, (T) null, exception);
    }

    public static <T> Action<T> inProgress() {
        return new Action<>(true, false, (T) null, null);
    }
}
