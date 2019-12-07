package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddExpenseEvent extends GwtEvent<AddExpenseEventHandler> {
    public static Type<AddExpenseEventHandler> TYPE = new Type<>();

    @Override
    public Type<AddExpenseEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddExpenseEventHandler handler) {
        handler.onAddExpense(this);
    }
}
