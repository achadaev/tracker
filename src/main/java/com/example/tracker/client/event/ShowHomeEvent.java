package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowHomeEvent extends GwtEvent<ShowHomeEventHandler> {
    public static final Type<ShowHomeEventHandler> TYPE = new Type<>();

    @Override
    public Type<ShowHomeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowHomeEventHandler handler) {
        handler.onShowHomeEvent(this);
    }
}
