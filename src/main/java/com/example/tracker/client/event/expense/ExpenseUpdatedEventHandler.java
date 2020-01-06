package com.example.tracker.client.event.expense;


import com.google.gwt.event.shared.EventHandler;

public interface ExpenseUpdatedEventHandler extends EventHandler {
    void onExpenseUpdated(ExpenseUpdatedEvent event);
}
