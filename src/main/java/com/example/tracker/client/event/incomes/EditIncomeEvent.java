package com.example.tracker.client.event.incomes;

import com.google.gwt.event.shared.GwtEvent;

public class EditIncomeEvent extends GwtEvent<EditIncomeEventHandler> {
    public static Type<EditIncomeEventHandler> TYPE = new Type<>();
    private int id;

    public EditIncomeEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Type<EditIncomeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditIncomeEventHandler handler) {
        handler.onEditIncome(this);
    }
}
