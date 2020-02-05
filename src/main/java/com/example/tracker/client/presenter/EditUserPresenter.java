package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.user.UserUpdatedEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.widget.PassChangeWidget;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.tracker.client.constant.WidgetConstants.*;
import static com.example.tracker.client.constant.PathConstants.LOGIN_PATH;

public class EditUserPresenter implements Presenter, PassChangeWidget.Changer {
    public interface Display {
        HasClickHandlers getSaveButton();
        TextBox getLogin();
        HasValue<String> getName();
        HasValue<String> getSurname();
        HasValue<String> getEmail();
        HasValue<String> getPassword();
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
    private boolean isLoginChanged = false;

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
                AlertWidget.alert(ERR, GETTING_USER_ERR).center();
            }

            @Override
            public void onSuccess(Method method, User response) {
                user = response;
                display.getLogin().setValue(user.getLogin());
                display.getName().setValue(user.getName());
                display.getSurname().setValue(user.getSurname());
                display.getEmail().setValue(user.getEmail());
                if (ExpensesGWTController.isAdmin()) {
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

        if (display.getChangePasswordButton() != null) {
            display.getChangePasswordButton().addClickHandler(clickEvent -> {
                PassChangeWidget passChangeWidget = new PassChangeWidget(this);
                passChangeWidget.change(SET_PASSWORD_LABEL).center();
            });
        }

        if (ExpensesGWTController.isAdmin()) {
            initRolesListBox(display.getRoleListBox());
        }
    }

    private boolean isPasswordValid(String password) {
        String pattern = PASSWORD_PATTERN;
        return password.matches(pattern);
    }

    @Override
    public void onChange(TextBox passBox, TextBox repeatPassBox) {
        if (passBox.getValue().equals(repeatPassBox.getValue())
            && isPasswordValid(passBox.getValue())) {
            user.setPassword(passBox.getValue());
            changePassword();
        } else {
            AlertWidget.alert(ERR, INCORRECT_PASSWORD_PATTERN_ERR).center();
        }
    }

    private void changePassword() {
        userWebService.updatePassword(user, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, CHANGING_PASSWORD_ERR).center();
            }

            @Override
            public void onSuccess(Method method, User response) {
                eventBus.fireEvent(new UserUpdatedEvent(response));
            }
        });
    }

    private void doSave() {
        if (!display.getLogin().getValue().equals(user.getLogin())
            && !ExpensesGWTController.isAdmin()) {
            isLoginChanged = true;
        }
        user.setLogin(display.getLogin().getValue());
        user.setName(display.getName().getValue());
        user.setSurname(display.getSurname().getValue());
        user.setEmail(display.getEmail().getValue());
        if (ExpensesGWTController.isAdmin()) {
            user.setRole(display.getRoleListBox().getSelectedItemText());
        } else {
            user.setRole(display.getRole().getText());
        }
        if (display.getPassword() != null) {
            user.setPassword(display.getPassword().getValue());
            user.setRegDate(new Date());
        } else {
            user.setRegDate(dateTimeFormat.parse(display.getRegDate().getText()));
        }

        userWebService.updateUser(user, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, UPDATING_USER_ERR).center();
            }

            @Override
            public void onSuccess(Method method, User response) {
                if (isLoginChanged) {
                    ExpensesGWTController.logout(userWebService);
                    Window.Location.replace(GWT.getHostPageBaseURL() + LOGIN_PATH);
                } else {
                    eventBus.fireEvent(new UserUpdatedEvent(response));
                }
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
