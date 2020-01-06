package com.example.tracker.client.presenter;

import com.example.tracker.client.event.user.AddUserEvent;
import com.example.tracker.client.event.user.EditUserEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ManageProfilesPresenter implements Presenter {

    private List<User> userList;

    public interface Display {
        HasClickHandlers getAddButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        List<Integer> getSelectedIds();
        void setData(List<User> userList);
        Widget asWidget();
    }

    private UserWebService userWebService;
    private HandlerManager eventBus;
    private Display display;

    public ManageProfilesPresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;
    }

    public void bind() {
        display.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new AddUserEvent());
            }
        });

        display.getEditButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                List<Integer> selectedIds = display.getSelectedIds();

                if (selectedIds.size() == 1) {
                    eventBus.fireEvent(new EditUserEvent(selectedIds.get(0)));
                } else {
                    Window.alert("Select one row");
                }
            }
        });

        display.getDeleteButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                deleteSelectedIds();
            }
        });
    }

    private void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        userWebService.deleteUsers(selectedIds, new MethodCallback<List<User>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error deleting users");
            }

            @Override
            public void onSuccess(Method method, List<User> response) {
                userList = response;
                display.setData(userList);
            }
        });
    }

    private void initTable() {
        userWebService.getAllUsers(new MethodCallback<List<User>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting all users");
            }

            @Override
            public void onSuccess(Method method, List<User> response) {
                userList = response;
                display.setData(userList);
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        initTable();
    }
}
