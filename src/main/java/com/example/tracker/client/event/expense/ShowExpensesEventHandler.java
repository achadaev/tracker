package com.example.tracker.client.event.expense;

import com.google.gwt.event.shared.EventHandler;

public interface ShowExpensesEventHandler extends EventHandler {
    void onShowExpensesEvent(ShowExpensesEvent event);
}
