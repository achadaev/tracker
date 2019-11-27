package com.example.tracker.client;

import com.example.tracker.client.view.mainPage.MainPageView;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.server.webServices.ExpenseWebServiceImpl;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
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
            public void onSuccess(Method method, List<Expense> response) {
                VerticalPanel panel = new VerticalPanel();
                for (Expense expense : response) {
                    Label label = new Label(expense.getName());
                    panel.add(label);
                }
                RootPanel.get().add(panel);
                RootPanel.get().add(new Label("Success"));
            }
            @Override
            public void onFailure(Method method, Throwable exception) {
                Label label = new Label(exception.getMessage());
                RootPanel.get().add(label);
                RootPanel.get().add(new Label("Error"));
            }
        });
    }
}
