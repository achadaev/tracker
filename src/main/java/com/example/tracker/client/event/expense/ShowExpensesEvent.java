package com.example.tracker.client.event.expense;

import com.google.gwt.event.shared.GwtEvent;

public class ShowExpensesEvent extends GwtEvent<ShowExpensesEventHandler> {
    public static final Type<ShowExpensesEventHandler> TYPE = new Type<>();

    @Override
    public Type<ShowExpensesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowExpensesEventHandler handler) {
        handler.onShowExpensesEvent(this);
    }
}
