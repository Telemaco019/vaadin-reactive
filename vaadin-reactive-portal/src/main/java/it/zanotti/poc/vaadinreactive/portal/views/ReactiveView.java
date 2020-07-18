package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.model.AppConstants;
import org.springframework.stereotype.Component;

/**
 * @author Michele Zanotti on 12/07/20
 **/
@Route(value = AppConstants.ROUTE_NAME_REACTIVE, layout = MainLayout.class)
@Component
@UIScope
public class ReactiveView extends VerticalLayout {
    public ReactiveView() {
        initGui();
    }

    private void initGui() {
        add(new Label("Test"));
    }
}
