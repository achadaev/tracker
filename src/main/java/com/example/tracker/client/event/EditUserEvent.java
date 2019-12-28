package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditUserEvent extends GwtEvent<EditUserEventHandler> {
    public static final Type<EditUserEventHandler> TYPE = new Type<>();
    private int id;

    public EditUserEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Type<EditUserEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditUserEventHandler handler) {
        handler.onEditUser(this);
    }
}
