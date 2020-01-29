package com.example.tracker.client;

import com.example.tracker.client.event.*;
import com.example.tracker.client.event.expense.*;
import com.example.tracker.client.event.incomes.AddIncomeEvent;
import com.example.tracker.client.event.incomes.EditIncomeEvent;
import com.example.tracker.client.event.incomes.IncomeUpdatedEvent;
import com.example.tracker.client.event.incomes.ShowIncomesEvent;
import com.example.tracker.client.event.type.*;
import com.example.tracker.client.event.user.*;
import com.example.tracker.client.message.AlertWidget;
import com.example.tracker.client.presenter.*;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.view.*;
import com.example.tracker.shared.model.ProcedureType;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensesGWTController implements Presenter, ValueChangeHandler<String> {

    private static User user;
    private static List<ProcedureType> expenseTypes;
    private static List<ProcedureType> incomeTypes;
    public static boolean isAdmin;

    private HandlerManager eventBus;
    private ProcedureWebService procedureWebService;
    private TypeWebService typeWebService;
    private UserWebService userWebService;
    private HasWidgets container;

    public ExpensesGWTController(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                                 UserWebService userWebService, HandlerManager eventBus) {
        this.eventBus = eventBus;
        this.userWebService = userWebService;
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;

        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting types").center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                expenseTypes = response;
            }
        });

        typeWebService.getIncomeTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting types").center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                incomeTypes = response;
            }
        });

        userWebService.getUser(new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                AlertWidget.alert("Error", "Error getting user").center();
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

        eventBus.addHandler(ShowIncomesEvent.TYPE, event -> doShowIncomes());

        eventBus.addHandler(ShowProfileEvent.TYPE, event -> doShowProfile());

        //TODO ShowCalendarEvent

        eventBus.addHandler(AddExpenseEvent.TYPE, event -> doAddNewExpense());

        eventBus.addHandler(AddIncomeEvent.TYPE, event -> doAddNewIncome());

        eventBus.addHandler(EditExpenseEvent.TYPE, event -> doEditExpense(event.getId()));

        eventBus.addHandler(EditIncomeEvent.TYPE, event -> doEditIncome(event.getId()));

        eventBus.addHandler(ExpenseUpdatedEvent.TYPE, event -> doExpenseUpdated());

        eventBus.addHandler(IncomeUpdatedEvent.TYPE, event -> doIncomeUpdated());

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
        History.newItem("add-expense");
    }

    private void doAddNewIncome() {
        History.newItem("add-income");
    }

    private void doAddNewUser() {
        History.newItem("add-user");
    }

    private void doAddNewType() {
        History.newItem("add-type");
    }

    private void doEditExpense(int id) {
        History.newItem("edit-expense", false);
        Presenter presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                new EditProcedureDialog(), id);
        presenter.go(container);
    }

    private void doEditIncome(int id) {
        History.newItem("edit-income", false);
        Presenter presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                new EditProcedureDialog(), id);
        presenter.go(container);
    }

    private void doEditUser(int id) {
        History.newItem("edit-user", false);
        Presenter presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(), id);
        presenter.go(container);
    }

    private void doEditType(int id) {
        History.newItem("edit-type", false);
        Presenter presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog(), id);
        presenter.go(container);
    }

    private void doExpenseUpdated() {
        History.newItem("expense-list");
    }

    private void doIncomeUpdated() {
        History.newItem("income-list");
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
        History.newItem("expense-list");
    }

    private void doShowProfile() {
        History.newItem("profile");
    }

    private void doShowIncomes() {
        History.newItem("income-list");
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
                presenter = new HomePresenter(procedureWebService, eventBus, new HomeView());
            }
            else if (token.equals("expense-list")) {
                presenter = new ExpensePresenter(procedureWebService, typeWebService, eventBus, new ProcedureView(procedureWebService));
            }
            else if (token.equals("income-list")) {
                presenter = new IncomePresenter(procedureWebService, typeWebService, eventBus, new ProcedureView(procedureWebService));
            }
            else if (token.equals("add-expense")) {
                presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog());
            }
            else if (token.equals("add-income")) {
                presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog());
            }
            else if (token.equals("add-user")) {
                presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView());
            }
            else if (token.equals("add-type")) {
                presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog());
            }
            else if (token.equals("profile")) {
                presenter = new ProfilePresenter(userWebService, eventBus, new ProfileView());
            }
            else if (token.equals("edit-expense")) {
                presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog());
            }
            else if (token.equals("edit-income")) {
                presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog());
            }
            else if (token.equals("edit-user")) {
                presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView());
            }
            else if (token.equals("edit-type")) {
                presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog());
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

    public static List<ProcedureType> getExpenseTypes() {
        return expenseTypes;
    }

    public static List<ProcedureType> getIncomeTypes() {
        return incomeTypes;
    }

}