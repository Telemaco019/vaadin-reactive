package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Michele Zanotti on 01/08/20
 **/
public class CreateTodoPanel extends HorizontalLayout {

    private Button createTodoButton;
    private TextField todoContentTexField;

    public CreateTodoPanel() {
        initGui();
    }

    private void initGui() {
        todoContentTexField = getCreateTodoTextField();
        add(todoContentTexField);

        createTodoButton = getCreateTodoButton();
        add(createTodoButton);

        setupListeners();
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

    private void setupListeners() {
        todoContentTexField.addValueChangeListener(e -> createTodoButton.setEnabled(StringUtils.isNotEmpty(e.getValue())));
        createTodoButton.addClickListener(e -> manageCreateTodo(todoContentTexField.getValue()));
    }

    private void manageCreateTodo(String todoContent) {
//        Disposable subscription = getTodoService().getTodoMonoById(todoId)
//                .switchIfEmpty(Mono.error(new NoSuchElementException(String.format("Todo with id %d not found", todoId))))
//                .subscribe(
//                        this::accessUIAndDrawTodo,
//                        this::accessUIAndShowErrorDialog
//                );
//
//        loadTodoByIdDisposableContainer.update(subscription);
    }
}
