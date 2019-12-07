package com.example.tracker.client.event;

import com.example.tracker.shared.model.Expense;
import com.google.gwt.event.shared.GwtEvent;

public class ExpenseUpdatedEvent extends GwtEvent<ExpenseUpdatedEventHandler> {
    public static Type<ExpenseUpdatedEventHandler> TYPE = new Type<>();
    private Expense updatedExpense;

    public ExpenseUpdatedEvent(Expense updatedExpense) {
        this.updatedExpense = updatedExpense;
    }

    public Expense getUpdatedExpense() {
        return updatedExpense;
    }

    @Override
    public Type<ExpenseUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ExpenseUpdatedEventHandler handler) {
        handler.onExpenseUpdated(this);
    }
}
