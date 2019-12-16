package com.example.tracker.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ProfileBarPresenter implements Presenter {
    public interface Display {
        Button getLogoutButton();
        Widget asWidget();
    }

    private Display display;

    public ProfileBarPresenter(Display display) {
        this.display = display;
    }

    @Override
    public void go(HasWidgets container) {
        container.add(display.asWidget());
        display.getLogoutButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Cookies.removeCookie("JSESSIONID");
                Window.Location.replace(GWT.getHostPageBaseURL() + "login");
            }
        });
    }
}
