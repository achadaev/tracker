package com.example.tracker.client.event.type;

import com.google.gwt.event.shared.GwtEvent;

public class AddTypeEvent extends GwtEvent<AddTypeEventHandler> {
    public static final Type<AddTypeEventHandler> TYPE = new Type<>();

    @Override
    public Type<AddTypeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddTypeEventHandler handler) {
        handler.onAddType(this);
    }
}
