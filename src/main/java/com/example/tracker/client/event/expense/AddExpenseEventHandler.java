package com.example.tracker.client.event.expense;

import com.google.gwt.event.shared.EventHandler;

public interface AddExpenseEventHandler extends EventHandler {
    void onAddExpense(AddExpenseEvent event);
}
