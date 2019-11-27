package com.example.tracker.client;

import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.mainPage.MainPageView;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensesGWTApplication implements EntryPoint {
    @Override
    public void onModuleLoad() {

        ExpenseWebService expenseWebService = GWT.create(ExpenseWebService.class);

        expenseWebService.getAllExpenses(new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                RootPanel.get().add(new Label("Error"));
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                for (Expense expense : response) {
                    RootPanel.get().add(new Label(expense.getName()));
                }
                RootPanel.get().add(new Label("Success"));
            }
        });
    }
}
