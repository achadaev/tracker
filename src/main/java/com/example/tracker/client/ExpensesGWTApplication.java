package com.example.tracker.client;

import com.example.tracker.client.services.ExpenseWebService;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;

public class ExpensesGWTApplication implements EntryPoint {
    @Override
    public void onModuleLoad() {
        ExpenseWebService expenseWebService = GWT.create(ExpenseWebService.class);
        HandlerManager eventBus = new HandlerManager(null);
        ExpensesGWTController expensesViewer = new ExpensesGWTController(expenseWebService, eventBus);
        expensesViewer.go(RootPanel.get());
    }
}
