package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.user.AddUserEvent;
import com.example.tracker.client.event.user.EditUserEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.widget.ConfirmWidget;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ManageProfilesPresenter implements Presenter, ConfirmWidget.Confirmation {

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

    private void confirmDeleting() {
        ConfirmWidget confirmWidget = new ConfirmWidget(this);
        confirmWidget.confirm("Confirmation", "Do you actually want to delete these fields?").center();
    }

    @Override
    public void onConfirm() {
        deleteSelectedIds();
    }

    public void bind() {
        display.getAddButton().addClickHandler(clickEvent -> eventBus.fireEvent(new AddUserEvent()));

        display.getEditButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() == 1) {
                eventBus.fireEvent(new EditUserEvent(selectedIds.get(0)));
            } else {
                AlertWidget.alert("Error", "Select one row").center();
            }
        });

        display.getDeleteButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();
            if (selectedIds.contains(ExpensesGWTController.getUser().getId()))
            {
                AlertWidget.alert("Error", "Unable to delete your own account").center();
            } else {
                confirmDeleting();
            }
        });
    }

    private void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        userWebService.archiveUsers(selectedIds, new MethodCallback<List<User>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error deleting users").center();
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
                AlertWidget.alert("Error", "Error getting all users").center();
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
