package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowProfileEvent extends GwtEvent<ShowProfileEventHandler> {
    public static final Type<ShowProfileEventHandler> TYPE = new Type<>();

    @Override
    public Type<ShowProfileEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowProfileEventHandler handler) {
        handler.onShowProfileEvent(this);
    }
}
