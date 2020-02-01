package com.example.tracker.client.presenter;

import com.example.tracker.client.event.type.TypeUpdatedEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class EditTypePresenter implements Presenter {

    public interface Display {
        HasClickHandlers getSaveButton();
        HasClickHandlers getCancelButton();
        HasValue<String> getName();
        RadioButton getExpenseRadio();
        RadioButton getIncomeRadio();
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
        display.getExpenseRadio().setValue(true);
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
                display.getName().setValue(type.getName());
                if (type.getKind() < 0) {
                    display.getExpenseRadio().setValue(true);
                } else {
                    display.getIncomeRadio().setValue(true);
                }
            }
        });
    }


    private void bind() {
        display.getSaveButton().addClickHandler(clickEvent -> doSave());
        display.getCancelButton().addClickHandler(clickEvent -> display.hideDialog());
    }

    private void doSave() {
        type.setName(display.getName().getValue());
        if (display.getExpenseRadio().getValue()) {
            type.setKind(-1);
        } else {
            type.setKind(1);
        }

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
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        display.showDialog();
    }
}
