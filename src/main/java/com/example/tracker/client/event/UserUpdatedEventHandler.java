package com.example.tracker.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface UserUpdatedEventHandler extends EventHandler {
    void onUserUpdated(UserUpdatedEvent event);
}
