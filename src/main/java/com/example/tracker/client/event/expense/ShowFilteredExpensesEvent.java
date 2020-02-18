package com.example.tracker.client.event.expense;

import com.google.gwt.event.shared.GwtEvent;

public class ShowFilteredExpensesEvent extends GwtEvent<ShowFilteredExpensesEventHandler> {
    public static Type<ShowFilteredExpensesEventHandler> TYPE = new Type<>();
    private int typeId;
    private boolean isOwn;

    public ShowFilteredExpensesEvent(int typeId, boolean isOwn) {
        this.typeId = typeId;
        this.isOwn = isOwn;
    }

    public int getTypeId() {
        return typeId;
    }

    public boolean isOwn() {
        return isOwn;
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
