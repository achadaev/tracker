package com.example.tracker.client.presenter;

import com.example.tracker.client.event.type.TypeUpdatedEvent;
import com.example.tracker.client.message.AlertWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class EditTypePresenter implements Presenter {

    public interface Display {
        HasClickHandlers getSaveButton();
        HasClickHandlers getCancelButton();
        HasValue<String> getName();
        HasValue<String> getKind();
        Widget asWidget();
        void showDialog();
        void hideDialog();
    }

    private ProcedureType type;
    private TypeWebService typeWebService;
    private HandlerManager eventBus;
    private Display display;

    public EditTypePresenter(TypeWebService typeWebService, HandlerManager eventBus, Display display) {
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.type = new ProcedureType();
    }

    public EditTypePresenter(TypeWebService typeWebService, HandlerManager eventBus, Display display, int id) {
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;

        typeWebService.getTypeById(id, new MethodCallback<ProcedureType>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting type").center();
            }

            @Override
            public void onSuccess(Method method, ProcedureType response) {
                type = response;
                EditTypePresenter.this.display.getName().setValue(type.getName());
                EditTypePresenter.this.display.getKind().setValue(Integer.toString(type.getKind()));
            }
        });
    }

    private void bind() {
        display.getSaveButton().addClickHandler(clickEvent -> doSave());
        display.getCancelButton().addClickHandler(clickEvent -> display.hideDialog());
    }

    private int validate(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            AlertWidget.alert("Error", "Incorrect Number Format").center();
        }
        return 0;
    }

    private void doSave() {
        int validatedKind = validate(display.getKind().getValue());
        if (validatedKind != 0) {
            type.setName(display.getName().getValue());
            type.setKind(validatedKind);

            typeWebService.updateType(type, new MethodCallback<ProcedureType>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error updating type").center();
                }

                @Override
                public void onSuccess(Method method, ProcedureType response) {
                    eventBus.fireEvent(new TypeUpdatedEvent(response));
                    display.hideDialog();
                }
            });
        } else {
            AlertWidget.alert("Error", "Kind should be > or < than 0").center();
        }
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        display.showDialog();
    }
}
