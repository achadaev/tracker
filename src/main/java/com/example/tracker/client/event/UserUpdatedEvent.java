package com.example.tracker.client.event;

import com.example.tracker.shared.model.User;
import com.google.gwt.event.shared.GwtEvent;

public class UserUpdatedEvent extends GwtEvent<UserUpdatedEventHandler> {
    public static final Type<UserUpdatedEventHandler> TYPE = new Type<>();
    private User updatedUser;

    public UserUpdatedEvent(User updatedUser) {
        this.updatedUser = updatedUser;
    }

    public User getUpdatedUser() {
        return updatedUser;
    }

    @Override
    public Type<UserUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(UserUpdatedEventHandler handler) {
        handler.onUserUpdated(this);
    }
}
