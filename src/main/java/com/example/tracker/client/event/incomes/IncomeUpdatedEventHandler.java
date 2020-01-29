package com.example.tracker.client.event.incomes;


import com.google.gwt.event.shared.EventHandler;

public interface IncomeUpdatedEventHandler extends EventHandler {
    void onIncomeUpdated(IncomeUpdatedEvent event);
}
