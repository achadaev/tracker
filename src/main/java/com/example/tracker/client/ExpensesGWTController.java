package com.example.tracker.client;

import com.example.tracker.client.presenter.MainPresenter;
import com.example.tracker.client.presenter.Presenter;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.MainView;
import com.google.gwt.user.client.ui.HasWidgets;

public class ExpensesGWTController implements Presenter {
    private ExpenseWebService expenseWebService;
    private HasWidgets container;

    public ExpensesGWTController(ExpenseWebService expenseWebService) {
        this.expenseWebService = expenseWebService;
        bind();
    }

    private void bind() {

    }

    public void go(HasWidgets container) {
        this.container = container;
        Presenter presenter = new MainPresenter(expenseWebService, new MainView());

        if (presenter != null) {
            presenter.go(container);
        }
    }
}