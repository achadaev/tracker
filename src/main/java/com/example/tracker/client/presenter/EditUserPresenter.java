package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.user.UserUpdatedEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.widget.Alert;
import com.example.tracker.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.tracker.client.constant.WidgetConstants.*;
import static com.example.tracker.client.constant.PathConstants.LOGIN_PATH;
import static com.example.tracker.server.constant.DBConstants.DATE_PATTERN;

public class EditUserPresenter implements Presenter {
    public interface Display {
        HasClickHandlers getSaveButton();
        HasClickHandlers getCancelButton();
        TextBox getLogin();
        HasValue<String> getName();
        HasValue<String> getSurname();
        HasValue<String> getEmail();
        HasValue<String> getPassword();
        HasClickHandlers getChangePasswordButton();
        Lead getRole();
        Select getRoleSelection();
        Lead getRegDate();
        HasValue<String> getNewPassword();
        HasValue<String> getConfirmPassword();
        void showEditModal();
        void hideEditModal();
        void showChangeModal();
        void hideChangeModal();
        Form getEditForm();
        Form getChangeForm();
        Button getDoChangePasswordButton();
        Widget asWidget();
    }

    private User user;
    private UserWebService userWebService;
    private HandlerManager eventBus;
    private Display display;
    private List<String> roles;
    private boolean isLoginChanged = false;

    private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(DATE_PATTERN);

    public EditUserPresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.user = new User();
        roles = new ArrayList<>();
        roles.add("admin");
        roles.add("user");

        if (ExpensesGWTController.isAdmin()) {
            initRoleSelection(display.getRoleSelection());
        }
    }

    public EditUserPresenter(UserWebService userWebService, HandlerManager eventBus, Display display, int id) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
        roles = new ArrayList<>();
        roles.add("admin");
        roles.add("user");

        if (ExpensesGWTController.isAdmin()) {
            initRoleSelection(display.getRoleSelection());
        }

        userWebService.getUserById(id, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_USER_ERR);
            }

            @Override
            public void onSuccess(Method method, User response) {
                user = response;
                display.getLogin().setValue(user.getLogin());
                display.getName().setValue(user.getName());
                display.getSurname().setValue(user.getSurname());
                display.getEmail().setValue(user.getEmail());
                if (ExpensesGWTController.isAdmin()) {
                    display.getRoleSelection().setValue(Integer.toString(roles.indexOf(user.getRole())));
                } else {
                    display.getRole().setText(user.getRole());
                }
                display.getRegDate().setText(dateTimeFormat.format(user.getRegDate()));
            }
        });
    }

    private void initRoleSelection(Select select) {
        for (String role : roles) {
            Option option = new Option();
            option.setContent(role);
            option.setValue(Integer.toString(roles.indexOf(role)));
            select.add(option);
        }
        select.refresh();
    }

    private void bind() {
        display.getSaveButton().addClickHandler(clickEvent -> {
            if (display.getEditForm().validate()) {
                doSave();
                display.hideEditModal();
            }
        });

        display.getCancelButton().addClickHandler(clickEvent -> display.hideEditModal());

        if (display.getChangePasswordButton() != null) {
            display.getChangePasswordButton().addClickHandler(clickEvent -> display.showChangeModal());
        }

        display.getDoChangePasswordButton().addClickHandler(clickEvent -> {
           if (display.getNewPassword().getValue().equals(display.getConfirmPassword().getValue())
                   && display.getChangeForm().validate()) {
               user.setPassword(display.getNewPassword().getValue());
               changePassword();
               display.hideChangeModal();
           }
        });
    }

    private void changePassword() {
        userWebService.updatePassword(user, new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, CHANGING_PASSWORD_ERR);
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
            user.setRole(display.getRoleSelection().getSelectedItem().getContent());
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
                Alert.alert(ERR, UPDATING_USER_ERR);
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
        display.showEditModal();
    }
}
