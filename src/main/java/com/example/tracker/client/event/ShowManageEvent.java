package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowManageEvent extends GwtEvent<ShowManageEventHandler> {
    public static final Type<ShowManageEventHandler> TYPE = new Type<>();

    @Override
    public Type<ShowManageEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowManageEventHandler handler) {
        handler.onShowManage(this);
    }
}
