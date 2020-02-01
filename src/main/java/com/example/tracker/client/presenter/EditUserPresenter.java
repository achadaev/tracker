package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.user.UserUpdatedEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.widget.PassChangeWidget;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.List;

public class EditUserPresenter implements Presenter, PassChangeWidget.Changer {
    public interface Display {
        HasClickHandlers getSaveButton();
        Label getLogin();
        HasValue<String> getName();
        HasValue<String> getSurname();
        HasValue<String> getEmail();
        HasClickHandlers getChangePasswordButton();
        Label getRole();
        ListBox getRoleListBox();
        Label getRegDate();
        Widget asWidget();
    }

    private User user;
    private UserWebService userWebService;
    private HandlerManager eventBus;
    private Display display;
    private List<String> roles;

    private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd-MM-yyyy hh:mm:ss");

    public EditUserPresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.user = new User();
        roles = new ArrayList<>();
        roles.add("admin");
        roles.add("user");
    }

    public EditUserPresenter(UserWebService userWebService, HandlerManager eventBus, Display display, int id) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        roles = new ArrayList<>();
        roles.add("admin");
        roles.add("user");

        userWebService.getUserById(id, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting user by id").center();
            }

            @Override
            public void onSuccess(Method method, User response) {
                user = response;
                display.getLogin().setText(user.getLogin());
                display.getName().setValue(user.getName());
                display.getSurname().setValue(user.getSurname());
                display.getEmail().setValue(user.getEmail());
                if (ExpensesGWTController.isAdmin) {
                    display.getRoleListBox().setItemSelected(roles.indexOf(user.getRole()), true);
                } else {
                    display.getRole().setText(user.getRole());
                }
                display.getRegDate().setText(dateTimeFormat.format(user.getRegDate()));
            }
        });
    }

    private void initRolesListBox(ListBox listBox) {
        for (int i = 0; i < roles.size(); i++) {
            listBox.addItem(roles.get(i), Integer.toString(i));
        }
    }

    private void bind() {
        display.getSaveButton().addClickHandler(clickEvent -> doSave());

        display.getChangePasswordButton().addClickHandler(clickEvent -> {
           PassChangeWidget passChangeWidget = new PassChangeWidget(this);
           passChangeWidget.change("Set new password").center();
        });

        if (ExpensesGWTController.isAdmin) {
            initRolesListBox(display.getRoleListBox());
        }
    }

    private boolean isValid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{5,}";
        return password.matches(pattern);
    }

    @Override
    public void onChange(TextBox passBox, TextBox repeatPassBox) {
        if (passBox.getValue().equals(repeatPassBox.getValue())
            && isValid(passBox.getValue())) {
            user.setPassword(passBox.getValue());
            doSave();
        } else {
            AlertWidget.alert("Error", "Password must contain al least 1 upper case letter, " +
                    "1 lower case letter, 1 digit and length >= 5").center();
        }
    }

    private void doSave() {
        if (ExpensesGWTController.isAdmin) {
            user.setRole(display.getRoleListBox().getSelectedItemText());
        } else {
            user.setRole(display.getRole().getText());
        }
        user.setLogin(display.getLogin().getText());
        user.setName(display.getName().getValue());
        user.setSurname(display.getSurname().getValue());
        user.setEmail(display.getEmail().getValue());
        user.setRegDate(dateTimeFormat.parse(display.getRegDate().getText()));

        userWebService.updateUser(user, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error updating user").center();
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
