package com.example.tracker.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface AddUserEventHandler extends EventHandler {
    void onAddUser(AddUserEvent event);
}
