package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class ProfilePresenter implements Presenter {
    public interface Display {
        PasswordTextBox getPasswordBox();
        HasClickHandlers getChangePassButton();
        Widget asWidget();
    }

    private User user;
    private Display display;
    private UserWebService userWebService;

    public ProfilePresenter(UserWebService userWebService, Display display) {
        this.display = display;
        this.userWebService = userWebService;
        this.user = ExpensesGWTController.getUser();
    }

    public void bind() {
        display.getChangePassButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //TODO realization
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }
}
