package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.zanotti.poc.vaadinreactive.core.services.TodoService;
import it.zanotti.poc.vaadinreactive.core.utils.AppConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Michele Zanotti on 12/07/20
 **/
@Component
@UIScope
@Route(value = AppConstants.ROUTE_NAME_BLOCKING, layout = MainLayout.class)
public class BlockingView extends VerticalLayout {
    private final TodoService todoService;

    @Autowired
    public BlockingView(TodoService todoService) {
        this.todoService = todoService;
    }
}
