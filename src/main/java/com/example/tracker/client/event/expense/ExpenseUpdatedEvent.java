package com.example.tracker.client.event.expense;

import com.example.tracker.shared.model.Procedure;
import com.google.gwt.event.shared.GwtEvent;

public class ExpenseUpdatedEvent extends GwtEvent<ExpenseUpdatedEventHandler> {
    public static Type<ExpenseUpdatedEventHandler> TYPE = new Type<>();
    private Procedure updatedProcedure;

    public ExpenseUpdatedEvent(Procedure updatedProcedure) {
        this.updatedProcedure = updatedProcedure;
    }

    public Procedure getUpdatedProcedure() {
        return updatedProcedure;
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
