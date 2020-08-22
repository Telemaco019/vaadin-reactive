package it.zanotti.poc.vaadinreactive.portal.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import it.zanotti.poc.vaadinreactive.core.model.Todo;

import java.time.format.DateTimeFormatter;

/**
 * @author Michele Zanotti on 19/07/20
 **/
public class TodoItem extends VerticalLayout {
    private Label creationDateLabel;
    private Label todoDescriptionLabel;
    private Todo model;

    public TodoItem() {
        initGui();
    }

    private void initGui() {
        creationDateLabel = new Label();
        todoDescriptionLabel = new Label();

        HorizontalLayout headerLayout = createHeaderLayout();
        add(headerLayout);

        VerticalLayout contentLayout = createContentLayout();
        add(contentLayout);

        getStyle().set("background-color", "#3B3B3B");
        setWidth("30%");
        setPadding(Boolean.TRUE);
    }

    private HorizontalLayout createHeaderLayout() {
        HorizontalLayout result = new HorizontalLayout();

        result.add(creationDateLabel);

        Button editButton = createEditButton();
        editButton.getStyle().set("margin-left", "auto"); // align to right
        result.add(editButton);

        Button deleteButton = createDeleteButton();
        result.add(deleteButton);

        result.setWidthFull();
        result.setSpacing(Boolean.FALSE);
        return result;
    }

    private Button createEditButton() {
        Button button = new Button(VaadinIcon.EDIT.create());
        button.getStyle().set("cursor", "pointer");
        return button;
    }

    private Button createDeleteButton() {
        Button button = new Button(VaadinIcon.TRASH.create());
        button.getStyle().set("cursor", "pointer");
        return button;
    }

    private VerticalLayout createContentLayout() {
        VerticalLayout layout = new VerticalLayout();
        layout.add(todoDescriptionLabel);
        layout.setPadding(Boolean.FALSE);
        return layout;
    }

    public void setModel(Todo model) {
        this.model = model;
        refreshGui();
    }

    private void refreshGui() {
        creationDateLabel.setText(model.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        todoDescriptionLabel.setText(model.getDescription());
    }
}
