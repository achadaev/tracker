package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditExpenseEvent extends GwtEvent<EditExpenseEventHandler> {
    public static Type<EditExpenseEventHandler> TYPE = new Type<>();
    private int id;

    public EditExpenseEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Type<EditExpenseEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditExpenseEventHandler handler) {
        handler.onEditExpense(this);
    }
}
