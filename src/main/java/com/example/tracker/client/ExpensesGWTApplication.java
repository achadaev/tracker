package com.example.tracker.client;

import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.services.UserWebService;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;

public class ExpensesGWTApplication implements EntryPoint {
    @Override
    public void onModuleLoad() {
        ExpenseWebService expenseWebService = GWT.create(ExpenseWebService.class);
        UserWebService userWebService = GWT.create(UserWebService.class);
        HandlerManager eventBus = new HandlerManager(null);
        new ExpensesGWTController(expenseWebService, userWebService, eventBus);
    }
}
