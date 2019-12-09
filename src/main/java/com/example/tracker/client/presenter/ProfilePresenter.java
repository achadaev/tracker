package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
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

    private static User user;
    private Display display;
    private UserWebService userWebService;

    public ProfilePresenter(UserWebService userWebService, Display display) {
        this.display = display;
        this.userWebService = userWebService;

        userWebService.getUser(new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error getting user");
            }

            @Override
            public void onSuccess(Method method, User response) {
                user = response;
            }
        });
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

    public static String getUsername() {
        return user.getLogin();
    }
}
