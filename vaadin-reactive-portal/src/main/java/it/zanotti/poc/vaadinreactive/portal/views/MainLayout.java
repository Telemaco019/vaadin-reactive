package it.zanotti.poc.vaadinreactive.portal.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.material.Material;
import org.apache.commons.compress.utils.Lists;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * @author Michele Zanotti on 12/07/20
 **/
@Push
@Theme(value = Material.class, variant = Material.DARK)
public class MainLayout extends AppLayout {

    private Tabs navigationMenu;

    @PostConstruct
    private void initGui() {
        navigationMenu = createNavigationMenu();
        FlexLayout navigationMenuContainer = createNavigationMenuContainer();
        addToNavbar(true, navigationMenuContainer);
    }

    private FlexLayout createNavigationMenuContainer() {
        FlexLayout container = new FlexLayout(navigationMenu);
        container.setWidthFull();
        container.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return container;
    }

    private Tabs createNavigationMenu() {
        Tabs tabs = new Tabs();
        tabs.getElement().setAttribute("margin", "auto");
        tabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        tabs.add(createNavigationTabs());
        return tabs;
    }

    private Tab[] createNavigationTabs() {
        List<Tab> tabs = Lists.newArrayList();

        Tab reactivePageTab = createTab("Todo List", VaadinIcon.BOLT, TodoListView.class);
        tabs.add(reactivePageTab);

        return tabs.toArray(new Tab[0]);
    }

    private Tab createTab(String title, VaadinIcon icon, Class<? extends Component> navigationTarget) {
        RouterLink routerLink = new RouterLink(title, navigationTarget);
        routerLink.add(icon.create());
        return new Tab(routerLink);
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        String target = RouteConfiguration.forSessionScope().getUrl(getContent().getClass());
        Arrays.stream(navigationMenu.getChildren().toArray())
                .filter(child -> child instanceof Tab)
                .filter(child -> tabFirstChildIsRouterLink((Tab) child))
                .filter(tab -> ((RouterLink) ((Tab) tab).getChildren().findFirst().get()).getHref().equalsIgnoreCase(target))
                .findAny()
                .ifPresent(tab -> navigationMenu.setSelectedTab((Tab) tab));
    }

    private boolean tabFirstChildIsRouterLink(Tab tab) {
        return tab.getChildren().findFirst().map(child -> child instanceof RouterLink).orElse(Boolean.FALSE);
    }
}