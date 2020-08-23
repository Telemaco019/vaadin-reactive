package it.zanotti.poc.vaadinreactive.portal.model;

import lombok.Data;

import java.util.Objects;

/**
 * @author Michele Zanotti on 14/08/20
 **/
@Data
public class EventProgress<T> {
    private final boolean inProgress;
    private final boolean success;
    private final Throwable exception;
    private final T model;

    private EventProgress(boolean inProgress, boolean success, T model, Throwable exception) {
        this.inProgress = inProgress;
        this.success = success;
        this.model = model;
        this.exception = exception;
    }

    public boolean isInError() {
        return !Objects.isNull(exception);
    }

    public static <T> EventProgress<T> success(T model) {
        return new EventProgress<>(false, true, model, null);
    }

    public static <T> EventProgress<T> failure(Throwable exception) {
        return new EventProgress<>(false, false, (T) null, exception);
    }

    public static <T> EventProgress<T> inProgress() {
        return new EventProgress<>(true, false, (T) null, null);
    }
}
