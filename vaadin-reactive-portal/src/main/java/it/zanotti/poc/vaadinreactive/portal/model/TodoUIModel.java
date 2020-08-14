package it.zanotti.poc.vaadinreactive.portal.model;

import it.zanotti.poc.vaadinreactive.core.model.Todo;

/**
 * @author Michele Zanotti on 14/08/20
 **/
public class TodoUIModel extends UIModel<Todo> {

    public TodoUIModel(boolean inProgress, boolean success, Todo model, Throwable exception) {
        super(inProgress, success, model, exception);
    }

    public static TodoUIModel inProgress() {
        return new TodoUIModel(true, false, null, null);
    }

    public static TodoUIModel success(Todo model) {
        return new TodoUIModel(false, true, model, null);
    }

    public static TodoUIModel failure(Throwable exception) {
        return new TodoUIModel(false, false, null, exception);
    }
}
