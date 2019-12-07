package com.example.tracker.client;

import com.example.tracker.client.event.AddExpenseEvent;
import com.example.tracker.client.event.AddExpenseEventHandler;
import com.example.tracker.client.event.ExpenseUpdatedEvent;
import com.example.tracker.client.event.ExpenseUpdatedEventHandler;
import com.example.tracker.client.presenter.EditExpensePresenter;
import com.example.tracker.client.presenter.ExpensePresenter;
import com.example.tracker.client.presenter.Presenter;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.EditExpenseView;
import com.example.tracker.client.view.ExpenseView;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class ExpensesGWTController implements Presenter, ValueChangeHandler<String> {
    private HandlerManager eventBus;
    private ExpenseWebService expenseWebService;
    private HasWidgets container;

    public ExpensesGWTController(ExpenseWebService expenseWebService, HandlerManager eventBus) {
        this.eventBus = eventBus;
        this.expenseWebService = expenseWebService;
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

    private void doExpenseUpdated() {
        History.newItem("list");
    }

    public void go(HasWidgets container) {
        this.container = container;

        if ("".equals(History.getToken())) {
            History.newItem("list");
        } else {
            History.fireCurrentHistoryState();
        }
        /*Presenter presenter = new MainPresenter(expenseWebService, new MainView());

        if (presenter != null) {
            presenter.go(container);
        }*/
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

            if (presenter != null) {
                presenter.go(container);
            }
        }
    }
}