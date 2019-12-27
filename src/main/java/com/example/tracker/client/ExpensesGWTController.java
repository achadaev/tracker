package com.example.tracker.client;

import com.example.tracker.client.event.*;
import com.example.tracker.client.presenter.*;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.view.*;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
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

        userWebService.getUser(new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error getting user");
            }

            @Override
            public void onSuccess(Method method, User response) {
                user = response;
                go(RootPanel.get());
            }
        });

        bind();
    }

    private void bind() {
        History.addValueChangeHandler(this);

        eventBus.addHandler(ShowHomeEvent.TYPE, new ShowHomeEventHandler() {
            @Override
            public void onShowHomeEvent(ShowHomeEvent event) {
                doShowHome();
            }
        });

        eventBus.addHandler(ShowExpensesEvent.TYPE, new ShowExpensesEventHandler() {
            @Override
            public void onShowExpensesEvent(ShowExpensesEvent event) {
                doShowExpenses();
            }
        });

        eventBus.addHandler(ShowProfileEvent.TYPE, new ShowProfileEventHandler() {
            @Override
            public void onShowProfileEvent(ShowProfileEvent event) {
                doShowProfile();
            }
        });

        //TODO ShowCalendarEvent

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

    private void doShowHome() {
        History.newItem("home");
    }

    private void doShowExpenses() {
        History.newItem("list");
    }

    private void doShowProfile() {
        History.newItem("profile");
    }

    public void go(HasWidgets container) {
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(eventBus, mainView);

        this.container = mainPresenter.getPanel();
        container.add(mainView);
        HomePresenter homePresenter = new HomePresenter(expenseWebService, eventBus, new HomeView());
        homePresenter.go(this.container);

        if ("".equals(History.getToken())) {
            History.newItem("home");
        } else {
            History.fireCurrentHistoryState();
        }
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();

        if (token != null) {
            Presenter presenter = null;

            if (token.equals("home")) {
                presenter = new HomePresenter(expenseWebService, eventBus, new HomeView());
            }
            else if (token.equals("list")) {
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