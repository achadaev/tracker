package com.example.tracker.client;

import com.example.tracker.client.view.mainPage.MainPageView;
import com.example.tracker.client.services.ExpenseWebService;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;

public class ExpensesGWTController {
    private final ExpenseWebService expenseWebService;
    private final HandlerManager eventHandler;

    private SimplePanel mainContainer;

    public ExpensesGWTController(ExpenseWebService expenseWebService, HandlerManager eventHandler) {
        this.expenseWebService = expenseWebService;
        this.eventHandler = eventHandler;
    }

/*
    private void bind() {
        History.addValueChangeHandler(this);

    }
*/
}
