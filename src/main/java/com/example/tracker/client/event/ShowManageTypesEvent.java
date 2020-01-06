package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowManageTypesEvent extends GwtEvent<ShowManageTypesEventHandler> {
    public static final Type<ShowManageTypesEventHandler> TYPE = new Type<>();

    @Override
    public Type<ShowManageTypesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowManageTypesEventHandler handler) {
        handler.onShowManage(this);
    }
}
