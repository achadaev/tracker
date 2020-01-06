package com.example.tracker.client.event.user;

import com.google.gwt.event.shared.GwtEvent;

public class AddUserEvent extends GwtEvent<AddUserEventHandler> {
    public static final Type<AddUserEventHandler> TYPE = new Type<>();

    @Override
    public Type<AddUserEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(AddUserEventHandler handler) {
        handler.onAddUser(this);
    }
}
