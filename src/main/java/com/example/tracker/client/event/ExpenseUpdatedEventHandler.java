package com.example.tracker.client.event;


import com.google.gwt.event.shared.EventHandler;

public interface ExpenseUpdatedEventHandler extends EventHandler {
    void onExpenseUpdated(ExpenseUpdatedEvent event);
}
