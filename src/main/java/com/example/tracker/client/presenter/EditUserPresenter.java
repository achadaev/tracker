package com.example.tracker.client.presenter;

import com.example.tracker.client.event.user.UserUpdatedEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class EditUserPresenter implements Presenter {
    public interface Display {
        HasClickHandlers getSaveButton();
        HasValue<String> getLogin();
        HasValue<String> getName();
        HasValue<String> getSurname();
        HasValue<String> getEmail();
        HasValue<String> getPassword();
        HasValue<String> getRole();
        DatePicker getRegDate();
        Widget asWidget();
    }

    private User user;
    private UserWebService userWebService;
    private HandlerManager eventBus;
    private Display display;

    public EditUserPresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.user = new User();
    }

    public EditUserPresenter(UserWebService userWebService, HandlerManager eventBus, Display display, int id) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;

        userWebService.getUserById(id, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting user by id");
            }

            @Override
            public void onSuccess(Method method, User response) {
                user = response;
                EditUserPresenter.this.display.getLogin().setValue(user.getLogin());
                EditUserPresenter.this.display.getName().setValue(user.getName());
                EditUserPresenter.this.display.getSurname().setValue(user.getSurname());
                EditUserPresenter.this.display.getEmail().setValue(user.getEmail());
                EditUserPresenter.this.display.getPassword().setValue(user.getPassword());
                EditUserPresenter.this.display.getRole().setValue(user.getRole());
                EditUserPresenter.this.display.getRegDate().setValue(user.getRegDate());
            }
        });
    }

    private void bind() {
        display.getSaveButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                doSave();
            }
        });
    }

    private void doSave() {
        user.setLogin(display.getLogin().getValue());
        user.setName(display.getName().getValue());
        user.setSurname(display.getSurname().getValue());
        user.setEmail(display.getEmail().getValue());
        user.setPassword(display.getPassword().getValue());
        user.setRole(display.getRole().getValue());
        user.setRegDate(display.getRegDate().getValue());

        userWebService.updateUser(user, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error updating user");
            }

            @Override
            public void onSuccess(Method method, User response) {
                eventBus.fireEvent(new UserUpdatedEvent(response));
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
