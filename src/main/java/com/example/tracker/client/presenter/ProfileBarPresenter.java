package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.ShowManageEvent;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProfileBarPresenter implements Presenter {
    public interface Display {
        Label getUsernameLabel();
        Button getManageButton();
        Button getLogoutButton();
        Widget asWidget();
    }

    HandlerManager eventBus;
    private Display display;

    public ProfileBarPresenter(HandlerManager eventBus, Display display) {
        this.eventBus = eventBus;
        this.display = display;
        bind();
    }

    private void bind() {
        display.getLogoutButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Cookies.removeCookie("JSESSIONID");
                Window.Location.replace(GWT.getHostPageBaseURL() + "login");
            }
        });

        display.getManageButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new ShowManageEvent());
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        container.add(display.asWidget());
        display.getUsernameLabel().setText(ExpensesGWTController.getUser().getLogin());
    }
}
