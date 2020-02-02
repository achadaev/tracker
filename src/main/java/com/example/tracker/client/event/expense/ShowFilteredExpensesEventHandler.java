package com.example.tracker.client.event.expense;

import com.google.gwt.event.shared.EventHandler;

public interface ShowFilteredExpensesEventHandler extends EventHandler {
    void onShowFilteredExpenses(ShowFilteredExpensesEvent event);
}
