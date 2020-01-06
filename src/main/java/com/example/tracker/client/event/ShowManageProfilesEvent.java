package com.example.tracker.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ShowManageProfilesEvent extends GwtEvent<ShowManageProfilesEventHandler> {
    public static final Type<ShowManageProfilesEventHandler> TYPE = new Type<>();

    @Override
    public Type<ShowManageProfilesEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ShowManageProfilesEventHandler handler) {
        handler.onShowManage(this);
    }
}
