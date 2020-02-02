package com.example.tracker.client.event.expense;

import com.google.gwt.event.shared.GwtEvent;

public class ShowFilteredExpensesEvent extends GwtEvent<ShowFilteredExpensesEventHandler> {
    public static Type<ShowFilteredExpensesEventHandler> TYPE = new Type<>();
    private int typeId;

    public ShowFilteredExpensesEvent(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    @Override
    public Type<ShowFilteredExpensesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowFilteredExpensesEventHandler handler) {
        handler.onShowFilteredExpenses(this);
    }

}
