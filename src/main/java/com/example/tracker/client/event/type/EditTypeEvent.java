package com.example.tracker.client.event.type;

import com.google.gwt.event.shared.GwtEvent;

public class EditTypeEvent extends GwtEvent<EditTypeEventHandler> {
    public static final Type<EditTypeEventHandler> TYPE = new Type<>();
    private int id;

    public EditTypeEvent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public Type<EditTypeEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(EditTypeEventHandler handler) {
        handler.onEditType(this);
    }
}
