package com.example.tracker.client;

import com.example.tracker.client.event.*;
import com.example.tracker.client.event.expense.*;
import com.example.tracker.client.event.incomes.AddIncomeEvent;
import com.example.tracker.client.event.incomes.EditIncomeEvent;
import com.example.tracker.client.event.incomes.IncomeUpdatedEvent;
import com.example.tracker.client.event.incomes.ShowIncomesEvent;
import com.example.tracker.client.event.type.*;
import com.example.tracker.client.event.user.*;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.presenter.*;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.view.*;
import com.example.tracker.shared.model.ProcedureType;
import com.example.tracker.shared.model.User;
import com.google.gwt.core.client.GWT;
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
import static com.example.tracker.client.constant.HistoryItems.*;

public class ExpensesGWTController implements Presenter, ValueChangeHandler<String> {

    private static User user;
    private static List<ProcedureType> expenseTypes;
    private static List<ProcedureType> incomeTypes;
    private static boolean isAdmin;

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

        eventBus.addHandler(ShowFilteredExpensesEvent.TYPE, event -> doShowFilteredExpenses(event.getTypeId()));

        eventBus.addHandler(ShowIncomesEvent.TYPE, event -> doShowIncomes());

        eventBus.addHandler(ShowProfileEvent.TYPE, event -> doShowProfile());

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
        History.newItem(ADD_EXPENSE);
    }

    private void doAddNewIncome() {
        History.newItem(ADD_INCOME);
    }

    private void doAddNewUser() {
        History.newItem(ADD_USER);
    }

    private void doAddNewType() {
        History.newItem(ADD_TYPE);
    }

    private void doEditExpense(int id) {
        History.newItem(EDIT_EXPENSE, false);
        Presenter presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                new EditProcedureDialog(-1), id);
        presenter.go(container);
    }

    private void doEditIncome(int id) {
        History.newItem(EDIT_INCOME, false);
        Presenter presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                new EditProcedureDialog(1), id);
        presenter.go(container);
    }

    private void doEditUser(int id) {
        History.newItem(EDIT_USER, false);
        Presenter presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(false), id);
        presenter.go(container);
    }

    private void doEditType(int id) {
        History.newItem(EDIT_TYPE, false);
        Presenter presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog(), id);
        presenter.go(container);
    }

    private void doExpenseUpdated() {
        History.newItem(EXPENSE_LIST);
    }

    private void doIncomeUpdated() {
        History.newItem(INCOME_LIST);
    }

    private void doUserUpdated() {
        if (isAdmin()) {
            History.newItem(MANAGE_PROFILES);
        } else {
            History.newItem(PROFILE);
        }
    }

    private void doTypeUpdated() {
        History.newItem(MANAGE_TYPES);
    }

    private void doShowHome() {
        History.newItem(HOME);
    }

    private void doShowExpenses() {
        History.newItem(EXPENSE_LIST);
    }

    private void doShowFilteredExpenses(int typeId) {
        History.newItem(FILTER_EXPENSE, false);
        Presenter presenter = new ExpensePresenter(procedureWebService, typeWebService, userWebService, eventBus,
                new ProcedureView(procedureWebService, typeId), typeId);
        presenter.go(container);
    }

    private void doShowProfile() {
        History.newItem(PROFILE);
    }

    private void doShowIncomes() {
        History.newItem(INCOME_LIST);
    }

    private void doShowManageProfiles() {
        History.newItem(MANAGE_PROFILES);
    }

    private void doShowManageTypes() {
        History.newItem(MANAGE_TYPES);
    }

    public void go(HasWidgets container) {
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(userWebService, eventBus, mainView);

        this.container = mainPresenter.getContentPanel();
        container.add(mainView);

        if ("".equals(History.getToken())) {
            History.newItem(HOME);
        } else {
            History.fireCurrentHistoryState();
        }
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();

        if (token != null) {
            Presenter presenter = null;

            if (token.equals(HOME)) {
                presenter = new HomePresenter(procedureWebService, eventBus, new HomeView(eventBus));
            }
            else if (token.equals(EXPENSE_LIST)) {
                presenter = new ExpensePresenter(procedureWebService, typeWebService, userWebService, eventBus,
                        new ProcedureView(procedureWebService));
            }
            else if (token.equals(INCOME_LIST)) {
                presenter = new IncomePresenter(procedureWebService, typeWebService, userWebService, eventBus, new ProcedureView(procedureWebService));
            }
            else if (token.equals(ADD_EXPENSE)) {
                presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(-1));
            }
            else if (token.equals(ADD_INCOME)) {
                presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(1));
            }
            else if (token.equals(ADD_USER)) {
                if (isAdmin()) {
                    presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(true));
                } else {
                    AlertWidget.alert("Error", "Access denied").center();
                }
            }
            else if (token.equals(ADD_TYPE)) {
                if (isAdmin()) {
                    presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog());
                } else {
                    AlertWidget.alert("Error", "Access denied").center();
                }
            }
            else if (token.equals(PROFILE)) {
                presenter = new ProfilePresenter(userWebService, eventBus, new ProfileView());
            }
            else if (token.equals(EDIT_EXPENSE)) {
                presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(-1));
            }
            else if (token.equals(EDIT_INCOME)) {
                presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(1));
            }
            else if (token.equals(EDIT_USER)) {
                presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(false));
            }
            else if (token.equals(EDIT_TYPE)) {
                if (isAdmin()) {
                    presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog());
                } else {
                    AlertWidget.alert("Error", "Access denied").center();
                }
            }
            else if (token.equals(MANAGE_PROFILES)) {
                if (isAdmin()) {
                    presenter = new ManageProfilesPresenter(userWebService, eventBus, new ManageProfilesView());
                } else {
                    AlertWidget.alert("Error", "Access denied").center();
                }
            }
            else if (token.equals(MANAGE_TYPES)) {
                if (isAdmin()) {
                    presenter = new ManageTypesPresenter(typeWebService, eventBus, new ManageTypesView());
                } else {
                    AlertWidget.alert("Error", "Access denied").center();
                }
            }
            if (presenter != null) {
                presenter.go(container);
            }
        }
    }

    public static User getUser() {
        return user;
    }

    public static boolean isAdmin() {
        return isAdmin;
    }

    public static List<ProcedureType> getExpenseTypes() {
        return expenseTypes;
    }

    public static List<ProcedureType> getIncomeTypes() {
        return incomeTypes;
    }

    public static void logout(UserWebService userWebService) {
        userWebService.logout(new MethodCallback<String>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error logging out").center();
            }

            @Override
            public void onSuccess(Method method, String s) {
                Window.Location.replace(GWT.getHostPageBaseURL() + "login");
            }
        });
    }
}