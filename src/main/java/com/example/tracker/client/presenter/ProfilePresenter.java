package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.user.EditUserEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class ProfilePresenter implements Presenter {
    public interface Display {
        HasClickHandlers getEditProfileButton();
        void setData(User user);
        Widget asWidget();
    }

    private UserWebService userWebService;
    private HandlerManager eventBus;
    private Display display;

    public ProfilePresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        bind();
    }

    public void bind() {
        display.getEditProfileButton().addClickHandler(clickEvent -> eventBus
                .fireEvent(new EditUserEvent(ExpensesGWTController.getUser().getId())));
    }

    private void updateUserInfo() {
        userWebService.getUser(new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                AlertWidget.alert("Error", "Error getting user").center();
            }

            @Override
            public void onSuccess(Method method, User response) {
                display.setData(response);
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
        updateUserInfo();
    }
}
