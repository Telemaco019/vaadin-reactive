package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import it.zanotti.poc.vaadinreactive.portal.model.SubmitTextEvent;
import it.zanotti.poc.vaadinreactive.portal.utils.RxVaadinBindings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Flux;

/**
 * @author Michele Zanotti on 01/08/20
 **/
@Slf4j
public class CreateTodoPanel extends HorizontalLayout {

    private Flux<SubmitTextEvent> onTodoCreateFlux;
    private TextField todoContentTexField;

    public CreateTodoPanel() {
        initGui();
    }

    private void initGui() {
        todoContentTexField = getCreateTodoTextField();
        add(todoContentTexField);

        Button createTodoButton = getCreateTodoButton();
        add(createTodoButton);

        todoContentTexField.addValueChangeListener(e -> createTodoButton.setEnabled(StringUtils.isNotBlank(e.getValue())));

        onTodoCreateFlux = RxVaadinBindings.onButtonClicks(createTodoButton)
                .map(ignored -> new SubmitTextEvent(todoContentTexField.getValue()));
    }

    private Button getCreateTodoButton() {
        Button resultButton = new Button("Create");

        resultButton.getStyle().set("cursor", "pointer");
        resultButton.addThemeVariants(ButtonVariant.MATERIAL_CONTAINED);
        resultButton.setEnabled(Boolean.FALSE);

        return resultButton;
    }

    private TextField getCreateTodoTextField() {
        TextField resultTextField = new TextField();

        resultTextField.setPlaceholder("Type here...");
        resultTextField.setValueChangeMode(ValueChangeMode.EAGER);

        return resultTextField;
    }

    public Flux<SubmitTextEvent> getOnTodoCreateFlux() {
        return onTodoCreateFlux;
    }

    public void clearTextfield() {
        todoContentTexField.setValue(StringUtils.EMPTY);
    }
}
