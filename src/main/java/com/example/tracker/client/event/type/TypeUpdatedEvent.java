package com.example.tracker.client.event.type;

import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.shared.GwtEvent;

public class TypeUpdatedEvent extends GwtEvent<TypeUpdatedEventHandler> {
    public static final Type<TypeUpdatedEventHandler> TYPE = new Type<>();
    private ProcedureType updatedType;

    public TypeUpdatedEvent(ProcedureType updatedType) {
        this.updatedType = updatedType;
    }

    public ProcedureType getUpdatedType() {
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
