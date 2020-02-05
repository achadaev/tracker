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
import static com.example.tracker.client.constant.PathConstants.*;
import static com.example.tracker.client.constant.WidgetConstants.*;

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
                AlertWidget.alert(ERR, GETTING_TYPES_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                expenseTypes = response;
            }
        });

        typeWebService.getIncomeTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_TYPES_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                incomeTypes = response;
            }
        });

        userWebService.getUser(new MethodCallback<User>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                AlertWidget.alert(ERR, GETTING_USER_ERR).center();
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
        History.newItem(ADD_EXPENSE_PATH);
    }

    private void doAddNewIncome() {
        History.newItem(ADD_INCOME_PATH);
    }

    private void doAddNewUser() {
        History.newItem(ADD_USER_PATH);
    }

    private void doAddNewType() {
        History.newItem(ADD_TYPE_PATH);
    }

    private void doEditExpense(int id) {
        History.newItem(EDIT_EXPENSE_PATH, false);
        Presenter presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                new EditProcedureDialog(-1), id);
        presenter.go(container);
    }

    private void doEditIncome(int id) {
        History.newItem(EDIT_INCOME_PATH, false);
        Presenter presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                new EditProcedureDialog(1), id);
        presenter.go(container);
    }

    private void doEditUser(int id) {
        History.newItem(EDIT_USER_PATH, false);
        Presenter presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(false), id);
        presenter.go(container);
    }

    private void doEditType(int id) {
        History.newItem(EDIT_TYPE_PATH, false);
        Presenter presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog(), id);
        presenter.go(container);
    }

    private void doExpenseUpdated() {
        History.newItem(EXPENSE_LIST_PATH);
    }

    private void doIncomeUpdated() {
        History.newItem(INCOME_LIST_PATH);
    }

    private void doUserUpdated() {
        if (isAdmin()) {
            History.newItem(MANAGE_PROFILES_PATH);
        } else {
            History.newItem(PROFILE_PATH);
        }
    }

    private void doTypeUpdated() {
        History.newItem(MANAGE_TYPES_PATH);
    }

    private void doShowHome() {
        History.newItem(HOME_PATH);
    }

    private void doShowExpenses() {
        History.newItem(EXPENSE_LIST_PATH);
    }

    private void doShowFilteredExpenses(int typeId) {
        History.newItem(FILTER_EXPENSE_PATH, false);
        Presenter presenter = new ExpensePresenter(procedureWebService, typeWebService, userWebService, eventBus,
                new ProcedureView(procedureWebService, typeId), typeId);
        presenter.go(container);
    }

    private void doShowProfile() {
        History.newItem(PROFILE_PATH);
    }

    private void doShowIncomes() {
        History.newItem(INCOME_LIST_PATH);
    }

    private void doShowManageProfiles() {
        History.newItem(MANAGE_PROFILES_PATH);
    }

    private void doShowManageTypes() {
        History.newItem(MANAGE_TYPES_PATH);
    }

    public void go(HasWidgets container) {
        MainView mainView = new MainView();
        MainPresenter mainPresenter = new MainPresenter(userWebService, eventBus, mainView);

        this.container = mainPresenter.getContentPanel();
        container.add(mainView);

        if ("".equals(History.getToken())) {
            History.newItem(HOME_PATH);
        } else {
            History.fireCurrentHistoryState();
        }
    }

    public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();

        if (token != null) {
            Presenter presenter = null;

            if (token.equals(HOME_PATH)) {
                presenter = new HomePresenter(procedureWebService, eventBus, new HomeView(eventBus));
            }
            else if (token.equals(EXPENSE_LIST_PATH)) {
                presenter = new ExpensePresenter(procedureWebService, typeWebService, userWebService, eventBus,
                        new ProcedureView(procedureWebService));
            }
            else if (token.equals(INCOME_LIST_PATH)) {
                presenter = new IncomePresenter(procedureWebService, typeWebService, userWebService, eventBus, new ProcedureView(procedureWebService));
            }
            else if (token.equals(ADD_EXPENSE_PATH)) {
                presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(-1));
            }
            else if (token.equals(ADD_INCOME_PATH)) {
                presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(1));
            }
            else if (token.equals(ADD_USER_PATH)) {
                if (isAdmin()) {
                    presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(true));
                } else {
                    AlertWidget.alert(ERR, ACCESS_DENIED_ERR).center();
                }
            }
            else if (token.equals(ADD_TYPE_PATH)) {
                if (isAdmin()) {
                    presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog());
                } else {
                    AlertWidget.alert(ERR, ACCESS_DENIED_ERR).center();
                }
            }
            else if (token.equals(PROFILE_PATH)) {
                presenter = new ProfilePresenter(userWebService, eventBus, new ProfileView());
            }
            else if (token.equals(EDIT_EXPENSE_PATH)) {
                presenter = new EditExpensePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(-1));
            }
            else if (token.equals(EDIT_INCOME_PATH)) {
                presenter = new EditIncomePresenter(procedureWebService, typeWebService, eventBus,
                                new EditProcedureDialog(1));
            }
            else if (token.equals(EDIT_USER_PATH)) {
                presenter = new EditUserPresenter(userWebService, eventBus, new EditUserView(false));
            }
            else if (token.equals(EDIT_TYPE_PATH)) {
                if (isAdmin()) {
                    presenter = new EditTypePresenter(typeWebService, eventBus, new EditTypeDialog());
                } else {
                    AlertWidget.alert(ERR, ACCESS_DENIED_ERR).center();
                }
            }
            else if (token.equals(MANAGE_PROFILES_PATH)) {
                if (isAdmin()) {
                    presenter = new ManageProfilesPresenter(userWebService, eventBus, new ManageProfilesView());
                } else {
                    AlertWidget.alert(ERR, ACCESS_DENIED_ERR).center();
                }
            }
            else if (token.equals(MANAGE_TYPES_PATH)) {
                if (isAdmin()) {
                    presenter = new ManageTypesPresenter(typeWebService, eventBus, new ManageTypesView());
                } else {
                    AlertWidget.alert(ERR, ACCESS_DENIED_ERR).center();
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
                AlertWidget.alert(ERR, throwable.getMessage()).center();
                AlertWidget.alert(ERR, LOGGING_OUT_ERR).center();
            }

            @Override
            public void onSuccess(Method method, String s) {
                Window.Location.replace(GWT.getHostPageBaseURL() + LOGIN_PATH);
            }
        });
    }
}