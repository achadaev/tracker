package com.example.tracker.client.event.incomes;

import com.google.gwt.event.shared.GwtEvent;

public class ShowFilteredIncomesEvent extends GwtEvent<ShowFilteredIncomesEventHandler> {
    public static Type<ShowFilteredIncomesEventHandler> TYPE = new Type<>();
    private int typeId;
    private boolean isOwn;

    public ShowFilteredIncomesEvent(int typeId, boolean isOwn) {
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
    public Type<ShowFilteredIncomesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowFilteredIncomesEventHandler handler) {
        handler.onShowFilteredIncomes(this);
    }

}
