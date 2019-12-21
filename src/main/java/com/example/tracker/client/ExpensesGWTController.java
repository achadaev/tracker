package com.example.tracker.client;

import com.example.tracker.client.event.*;
import com.example.tracker.client.presenter.EditExpensePresenter;
import com.example.tracker.client.presenter.ExpensePresenter;
import com.example.tracker.client.presenter.Presenter;
import com.example.tracker.client.presenter.ProfilePresenter;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.view.EditExpenseView;
import com.example.tracker.client.view.ExpenseView;
import com.example.tracker.client.view.ProfileView;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensesGWTController implements Presenter, ValueChangeHandler<String> {

    private static User user;
    private static List<ExpenseType> types;

    private HandlerManager eventBus;
    private ExpenseWebService expenseWebService;
    private UserWebService userWebService;
    private HasWidgets container;

    public ExpensesGWTController(ExpenseWebService expenseWebService, UserWebService userWebService, HandlerManager eventBus) {
        this.eventBus = eventBus;
        this.userWebService = userWebService;
        this.expenseWebService = expenseWebService;

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

        expenseWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting types");
            }

            @Override
            public void onSuccess(Method method, List<ExpenseType> response) {
                types = response;
            }
        });

        bind();
    }

    private void bind() {
        History.addValueChangeHandler(this);

        eventBus.addHandler(AddExpenseEvent.TYPE, new AddExpenseEventHandler() {
            @Override
            public void onAddExpense(AddExpenseEvent event) {
                doAddNewExpense();
            }
        });

        eventBus.addHandler(EditExpenseEvent.TYPE, new EditExpenseEventHandler() {
            @Override
            public void onEditExpense(EditExpenseEvent event) {
                doEditExpense(event.getId());
            }
        });

        eventBus.addHandler(ExpenseUpdatedEvent.TYPE, new ExpenseUpdatedEventHandler() {
            @Override
            public void onExpenseUpdated(ExpenseUpdatedEvent event) {
                doExpenseUpdated();
            }
        });

        eventBus.addHandler(ShowProfileEvent.TYPE, new ShowProfileEventHandler() {
            @Override
            public void onShowProfileEvent(ShowProfileEvent event) {
                doShowProfile();
            }
        });
    }

    private void doAddNewExpense() {
        History.newItem("add");
    }

    private void doEditExpense(int id) {
        History.newItem("edit", false);
        Presenter presenter = new EditExpensePresenter(expenseWebService, eventBus, new EditExpenseView(), id);
        presenter.go(container);
    }

    private void doExpenseUpdated() {
        History.newItem("list");
    }

    private void doShowProfile() {
        History.newItem("profile");
    }

    public void go(HasWidgets container) {
        this.container = container;

        if ("".equals(History.getToken())) {
            History.newItem("list");
        } else {
            History.fireCurrentHistoryState();
        }
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();

        if (token != null) {
            Presenter presenter = null;

            if (token.equals("list")) {
                presenter = new ExpensePresenter(expenseWebService, eventBus, new ExpenseView());
            }
            else if (token.equals("add")) {
                presenter = new EditExpensePresenter(expenseWebService, eventBus, new EditExpenseView());
            }
            else if (token.equals("profile")) {
                presenter = new ProfilePresenter(userWebService, new ProfileView());
            }
            else if (token.equals("edit")) {
                presenter = new EditExpensePresenter(expenseWebService, eventBus, new EditExpenseView());
            }

            if (presenter != null) {
                presenter.go(container);
            }
        }
    }

    public static User getUser() {
        return user;
    }

    public static List<ExpenseType> getTypes() {
        return types;
    }
}