package com.example.tracker.client.event.type;

import com.google.gwt.event.shared.EventHandler;

public interface TypeUpdatedEventHandler extends EventHandler {
    void onTypeUpdated(TypeUpdatedEvent event);
}
