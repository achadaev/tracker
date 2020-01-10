package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.ShowManageProfilesEvent;
import com.example.tracker.client.event.ShowManageTypesEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class MenuBarPresenter implements Presenter {
    public interface Display {
        Label getUsernameLabel();
        Button getManageProfilesButton();
        Button getManageTypesButton();
        Button getLogoutButton();
        Widget asWidget();
    }

    HandlerManager eventBus;
    private Display display;

    public MenuBarPresenter(HandlerManager eventBus, Display display) {
        this.eventBus = eventBus;
        this.display = display;
        bind();
    }

    private void bind() {
        display.getLogoutButton().addClickHandler(clickEvent -> {
            Cookies.removeCookie("JSESSIONID");
            Window.Location.replace(GWT.getHostPageBaseURL() + "login");
        });

        display.getManageProfilesButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowManageProfilesEvent()));

        display.getManageTypesButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowManageTypesEvent()));
    }

    @Override
    public void go(HasWidgets container) {
        container.add(display.asWidget());
        display.getUsernameLabel().setText(ExpensesGWTController.getUser().getLogin());
    }
}
