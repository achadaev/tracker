package com.example.tracker.client.presenter;

import com.example.tracker.client.event.expense.ExpenseUpdatedEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class EditExpensePresenter implements Presenter {
    public interface Display {
        HasClickHandlers getSaveButton();
        HasClickHandlers getCancelButton();
        ListBox getTypeId();
        HasValue<String> getName();
        DatePicker getDate();
        HasValue<String> getPrice();
        Widget asWidget();
        void showDialog();
        void hideDialog();
    }

    protected Procedure procedure;
    protected ProcedureWebService procedureWebService;
    protected TypeWebService typeWebService;
    protected HandlerManager eventBus;
    protected Display display;

    public EditExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                                HandlerManager eventBus, Display display) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.procedure = new Procedure();
        bind();
    }

    public EditExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                                HandlerManager eventBus, Display display, int id) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;
        bind();

        procedureWebService.getProcedureById(id, new MethodCallback<Procedure>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                AlertWidget.alert("Error", "Error getting procedure").center();
            }

            @Override
            public void onSuccess(Method method, Procedure response) {
                procedure = response;
                EditExpensePresenter.this.display.getTypeId().setItemSelected(procedure.getTypeId() - 1, true);
                EditExpensePresenter.this.display.getName().setValue(procedure.getName());
                EditExpensePresenter.this.display.getDate().setValue(procedure.getDate());
                EditExpensePresenter.this.display.getPrice().setValue(Double.toString(procedure.getPrice()));
            }
        });
    }

    protected void initTypesListBox(ListBox listBox) {
        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", throwable.getMessage()).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> procedureTypes) {
                for (ProcedureType type : procedureTypes) {
                    listBox.addItem(type.getName(), Integer.toString(type.getId()));
                }
            }
        });
    }

    public void bind() {
        this.display.getSaveButton().addClickHandler(clickEvent -> doSave());
        this.display.getCancelButton().addClickHandler(clickEvent -> display.hideDialog());
        initTypesListBox(this.display.getTypeId());
    }

    public void go(HasWidgets container) {
        display.showDialog();
    }

    protected double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            AlertWidget.alert("Error", "Input correct price").center();
        }
        return 0.0;
    }

    protected void doSave() {
        if (!"".equals(display.getName().getValue())
                && display.getDate().getValue() != null
                && toDouble(display.getPrice().getValue()) != 0.0) {

            procedure.setTypeId(Integer.parseInt(display.getTypeId().getSelectedValue()));
            procedure.setKind(-1);
            procedure.setName(display.getName().getValue());
            procedure.setDate(display.getDate().getValue());
            procedure.setPrice(Double.parseDouble(display.getPrice().getValue()));

            procedureWebService.updateProcedure(procedure, new MethodCallback<Procedure>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    AlertWidget.alert("Error", "Error updating expense").center();
                }

                @Override
                public void onSuccess(Method method, Procedure response) {
                    eventBus.fireEvent(new ExpenseUpdatedEvent(response));
                    display.hideDialog();
                }
            });
        } else {
            AlertWidget.alert("Error", "Fill all fields").center();
        }
    }
}
