package com.example.tracker.client;

import com.example.tracker.client.event.*;
import com.example.tracker.client.event.expense.*;
import com.example.tracker.client.event.type.*;
import com.example.tracker.client.event.user.*;
import com.example.tracker.client.presenter.*;
import com.example.tracker.client.services.TypeWebService;
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
    public static boolean isAdmin;

    private HandlerManager eventBus;
    private ExpenseWebService expenseWebService;
    private TypeWebService typeWebService;
    private UserWebService userWebService;
    private HasWidgets container;

    public ExpensesGWTController(ExpenseWebService expenseWebService, TypeWebService typeWebService,
                                 UserWebService userWebService, HandlerManager eventBus) {
        this.eventBus = eventBus;
        this.userWebService = userWebService;
        this.expenseWebService = expenseWebService;
        this.typeWebService = typeWebService;

        typeWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
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
                isAdmin = "admin".equals(user.getRole());
                go(RootPanel.get());
            }
        });

        bind();
    }

    private void bind() {
        History.addValueChangeHandler(this);

        eventBus.addHandler(ShowHomeEvent.TYPE, event -> doShowHome());

        eventBus.addHandler(ShowExpensesEvent.TYPE, event -> doShowExpenses());

        eventBus.addHandler(ShowProfileEvent.TYPE, event -> doShowProfile());

        //TODO ShowCalendarEvent

        eventBus.addHandler(AddExpenseEvent.TYPE, event -> doAddNewExpense());

        eventBus.addHandler(EditExpenseEvent.TYPE, event -> doEditExpense(event.getId()));

        eventBus.addHandler(ExpenseUpdatedEvent.TYPE, event -> doExpenseUpdated());

        eventBus.addHandler(ShowManageProfilesEvent.TYPE, event -> doShowManageProfiles());

        eventBus.addHandler(AddUserEvent.TYPE, event -> doAddNewUser());

        eventBus.addHandler(EditUserEvent.TYPE, event -> doEditUser(event.getId()));

        eventBus.addHandler(UserUpdatedEvent.TYPE, event -> doUserUpdated());

        eventBus.addHandler(ShowManageTypesEvent.TYPE, event -> doShowManageTypes());

        eventBus.addHandler(AddTypeEvent.TYPE, event -> doAddNewType());

        eventBus.addHandler(EditTypeEvent.TYPE, event -> doEditType(event.getId()));

        eventBus.addHandler(TypeUpdatedEvent.TYPE, event -> doTypeUpdated());
    }

    private void doAddNewExpense() {
        History.newItem("add");
    }

    private void doAddNewUser() {
        History.newItem("add-user");
    }

    private void doAddNewType() {
        History.newItem("add-type");
    }

    private void doEditExpense(int id) {
        History.newItem("edit", false);
        Presenter presenter = new EditExpensePresenter(expenseWebService, typeWebService, eventBus,
                new EditExpenseView(), id);
        presenter.go(container);
    }

    private void doEditUser(int id) {
        History.newItem("edit-user", false);
        Presenter presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(), id);
        presenter.go(container);
    }

    private void doEditType(int id) {
        History.newItem("edit-type", false);
        Presenter presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeView(), id);
        presenter.go(container);
    }

    private void doExpenseUpdated() {
        History.newItem("list");
    }

    private void doUserUpdated() {
        if (isAdmin) {
            History.newItem("manage-profiles");
        } else {
            History.newItem("profile");
        }
    }

    private void doTypeUpdated() {
        History.newItem("manage-types");
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

    private void doShowManageProfiles() {
        History.newItem("manage-profiles");
    }

    private void doShowManageTypes() {
        History.newItem("manage-types");
    }

    public void go(HasWidgets container) {
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(eventBus, mainView);

        this.container = mainPresenter.getPanel();
        container.add(mainView);

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
                presenter = new ExpensePresenter(expenseWebService, typeWebService, eventBus, new ExpenseView());
            }
            else if (token.equals("add")) {
                presenter = new EditExpensePresenter(expenseWebService, typeWebService, eventBus,
                                new EditExpenseView());
            }
            else if (token.equals("add-user")) {
                presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView());
            }
            else if (token.equals("add-type")) {
                presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeView());
            }
            else if (token.equals("profile")) {
                presenter = new ProfilePresenter(userWebService, eventBus, new ProfileView());
            }
            else if (token.equals("edit")) {
                presenter = new EditExpensePresenter(expenseWebService, typeWebService, eventBus,
                                new EditExpenseView());
            }
            else if (token.equals("edit-user")) {
                presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView());
            }
            else if (token.equals("edit-type")) {
                presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeView());
            }
            else if (token.equals("manage-profiles")) {
                presenter = new ManageProfilesPresenter(userWebService, eventBus, new ManageProfilesView());
            }
            else if (token.equals("manage-types")) {
                presenter = new ManageTypesPresenter(typeWebService, eventBus, new ManageTypesView());
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