package com.example.tracker.client.event.incomes;

import com.google.gwt.event.shared.GwtEvent;

public class ShowFilteredIncomesEvent extends GwtEvent<ShowFilteredIncomesEventHandler> {
    public static Type<ShowFilteredIncomesEventHandler> TYPE = new Type<>();
    private int typeId;

    public ShowFilteredIncomesEvent(int typeId) {
        this.typeId = typeId;
    }

    public int getTypeId() {
        return typeId;
    }

    @Override
    public Type<ShowFilteredIncomesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowFilteredIncomesEventHandler handler) {
        handler.onShowFilteredIncomes(this);
    }

}
