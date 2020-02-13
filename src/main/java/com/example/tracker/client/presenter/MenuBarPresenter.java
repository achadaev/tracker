package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.ShowManageProfilesEvent;
import com.example.tracker.client.event.ShowManageTypesEvent;
import com.example.tracker.client.services.UserWebService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.gwtbootstrap3.client.ui.AnchorListItem;

public class MenuBarPresenter implements Presenter {
    public interface Display {
        AnchorListItem getManageProfilesButton();
        AnchorListItem getManageTypesButton();
        AnchorListItem getLogoutButton();
        Widget asWidget();
    }

    UserWebService userWebService;
    HandlerManager eventBus;
    private Display display;

    public MenuBarPresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        bind();
    }

    private void bind() {
        display.getLogoutButton().addClickHandler(clickEvent -> {
            ExpensesGWTController.logout(userWebService);
        });

        display.getManageProfilesButton().addClickHandler(clickEvent ->
                eventBus.fireEvent(new ShowManageProfilesEvent()));

        display.getManageTypesButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowManageTypesEvent()));
    }

    public AnchorListItem getManageTypesButton() {
        return display.getManageTypesButton();
    }

    public AnchorListItem getManageProfilesButton() {
        return display.getManageProfilesButton();
    }

    public AnchorListItem getLogoutButton() {
        return display.getLogoutButton();
    }

    @Override
    public void go(HasWidgets container) {
        container.add(display.asWidget());
        if (!ExpensesGWTController.isAdmin()) {
            display.getManageTypesButton().removeFromParent();
            display.getManageProfilesButton().removeFromParent();
        }
    }
}
