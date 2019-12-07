package com.example.tracker.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface EditExpenseEventHandler extends EventHandler {
    void onEditExpense(EditExpenseEvent event);
}
