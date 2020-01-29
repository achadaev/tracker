package com.example.tracker.client.event.incomes;

import com.google.gwt.event.shared.GwtEvent;

public class AddIncomeEvent extends GwtEvent<AddIncomeEventHandler> {
    public static Type<AddIncomeEventHandler> TYPE = new Type<>();

    @Override
    public Type<AddIncomeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddIncomeEventHandler handler) {
        handler.onAddIncome(this);
    }
}
