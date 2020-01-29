package com.example.tracker.client.event.incomes;

import com.example.tracker.shared.model.Procedure;
import com.google.gwt.event.shared.GwtEvent;

public class IncomeUpdatedEvent extends GwtEvent<IncomeUpdatedEventHandler> {
    public static Type<IncomeUpdatedEventHandler> TYPE = new Type<>();
    private Procedure updatedProcedure;

    public IncomeUpdatedEvent(Procedure updatedProcedure) {
        this.updatedProcedure = updatedProcedure;
    }

    public Procedure getUpdatedProcedure() {
        return updatedProcedure;
    }

    @Override
    public Type<IncomeUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(IncomeUpdatedEventHandler handler) {
        handler.onIncomeUpdated(this);
    }
}
