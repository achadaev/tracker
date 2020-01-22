package com.example.tracker.client.presenter;

import com.example.tracker.client.event.type.TypeUpdatedEvent;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
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
        Widget asWidget();
        void showDialog();
        void hideDialog();
    }

    private ExpenseType type;
    private TypeWebService typeWebService;
    private HandlerManager eventBus;
    private Display display;

    public EditTypePresenter(TypeWebService typeWebService, HandlerManager eventBus, Display display) {
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.type = new ExpenseType();
    }

    public EditTypePresenter(TypeWebService typeWebService, HandlerManager eventBus, Display display, int id) {
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;

        typeWebService.getTypeById(id, new MethodCallback<ExpenseType>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting type");
            }

            @Override
            public void onSuccess(Method method, ExpenseType response) {
                type = response;
                EditTypePresenter.this.display.getName().setValue(type.getName());
            }
        });
    }

    private void bind() {
        display.getSaveButton().addClickHandler(clickEvent -> doSave());
        display.getCancelButton().addClickHandler(clickEvent -> display.hideDialog());
    }

    private void doSave() {
        type.setName(display.getName().getValue());

        typeWebService.updateType(type, new MethodCallback<ExpenseType>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error updating type");
            }

            @Override
            public void onSuccess(Method method, ExpenseType response) {
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
