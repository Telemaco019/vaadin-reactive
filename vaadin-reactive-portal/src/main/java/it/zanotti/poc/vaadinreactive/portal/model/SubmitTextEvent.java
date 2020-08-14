package it.zanotti.poc.vaadinreactive.portal.model;

/**
 * @author Michele Zanotti on 12/08/20
 **/
public class SubmitTextEvent extends UIEvent<String> {
    public SubmitTextEvent(String submittedText) {
        super(submittedText);
    }
}
