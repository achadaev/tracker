package com.example.tracker.client.presenter;

import com.example.tracker.client.event.expense.ExpenseUpdatedEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ListBox;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class EditIncomePresenter extends EditExpensePresenter {

    public EditIncomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService, HandlerManager eventBus, Display display) {
        super(procedureWebService, typeWebService, eventBus, display);
    }

    public EditIncomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService, HandlerManager eventBus, Display display, int id) {
        super(procedureWebService, typeWebService, eventBus, display, id);
    }

    @Override
    protected void initTypesListBox(ListBox listBox) {
        typeWebService.getIncomeTypes(new MethodCallback<List<ProcedureType>>() {
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

    @Override
    protected void doSave() {
        if (!"".equals(display.getName().getValue())
                && display.getDate().getValue() != null
                && toDouble(display.getPrice().getValue()) != 0.0) {

            procedure.setTypeId(Integer.parseInt(display.getTypeId().getSelectedValue()));
            procedure.setKind(1);
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
                    Window.Location.replace(GWT.getHostPageBaseURL() + "#income-list");
                }
            });
        } else {
            AlertWidget.alert("Error", "Fill all fields").center();
        }
    }
}
