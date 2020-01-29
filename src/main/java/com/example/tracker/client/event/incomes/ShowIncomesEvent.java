package com.example.tracker.client.event.incomes;

import com.google.gwt.event.shared.GwtEvent;

public class ShowIncomesEvent extends GwtEvent<ShowIncomesEventHandler> {
    public static final GwtEvent.Type<ShowIncomesEventHandler> TYPE = new GwtEvent.Type<>();

    @Override
    public GwtEvent.Type<ShowIncomesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowIncomesEventHandler handler) {
        handler.onShowIncomesEvent(this);
    }

}
