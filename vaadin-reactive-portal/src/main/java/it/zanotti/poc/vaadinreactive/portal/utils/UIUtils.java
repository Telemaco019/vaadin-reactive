package it.zanotti.poc.vaadinreactive.portal.utils;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;

/**
 * @author Michele Zanotti on 22/08/20
 **/
public class UIUtils {
    public static void showDialogWithMessage(String message) {
        Dialog dialog = new Dialog();
        dialog.add(new Label(message));
        dialog.open();
    }
}
