package com.example.tracker.client.event.type;

import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.shared.GwtEvent;

public class TypeUpdatedEvent extends GwtEvent<TypeUpdatedEventHandler> {
    public static final Type<TypeUpdatedEventHandler> TYPE = new Type<>();
    private ExpenseType updatedType;

    public TypeUpdatedEvent(ExpenseType updatedType) {
        this.updatedType = updatedType;
    }

    public ExpenseType getUpdatedType() {
        return updatedType;
    }

    @Override
    public Type<TypeUpdatedEventHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(TypeUpdatedEventHandler handler) {
        handler.onTypeUpdated(this);
    }
}
