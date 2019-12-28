package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ProfilePresenter implements Presenter {
    public interface Display {
        PasswordTextBox getPasswordBox();
        HasClickHandlers getChangePassButton();
        void setData(User user);
        Widget asWidget();
    }

    private Display display;
    private UserWebService userWebService;

    public ProfilePresenter(UserWebService userWebService, Display display) {
        this.display = display;
        this.userWebService = userWebService;
/*
            userWebService.getAllUsers(new MethodCallback<List<User>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    Window.alert("Error getting all users");
                }

                @Override
                public void onSuccess(Method method, List<User> response) {
                    users = response;

                }
            });
*/
        bind();
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
        container.clear();
        container.add(display.asWidget());
        display.setData(ExpensesGWTController.getUser());
    }
}
