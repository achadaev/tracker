package com.example.tracker.client.presenter;

import com.example.tracker.client.event.incomes.IncomeUpdatedEvent;
import com.example.tracker.client.widget.Alert;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.shared.HandlerManager;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.List;

import static com.example.tracker.client.constant.WidgetConstants.*;

public class EditIncomePresenter extends EditExpensePresenter {

    public EditIncomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService, HandlerManager eventBus, Display display) {
        super(procedureWebService, typeWebService, eventBus, display);
    }

    public EditIncomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService, HandlerManager eventBus, Display display, int id) {
        super(procedureWebService, typeWebService, eventBus, display, id);
    }

    @Override
    protected void initTypesListBox(Select select) {
        typeWebService.getIncomeTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_TYPES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                setTypes(display.getTypeId(), response);
            }
        });
    }

    @Override
    protected void doSave() {
        if (!"".equals(display.getName().getValue())
                && display.getDate().getValue() != null
                && toDouble(display.getPrice().getValue()) != 0.0) {

            procedure.setTypeId(Integer.parseInt(display.getTypeId().getValue()));
            procedure.setKind(1);
            procedure.setName(display.getName().getValue());
            procedure.setDate(display.getDate().getValue());
            procedure.setPrice(Double.parseDouble(display.getPrice().getValue()));

            procedureWebService.updateProcedure(procedure, new MethodCallback<Procedure>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    Alert.alert(ERR, UPDATING_INCOME_ERR);
                }

                @Override
                public void onSuccess(Method method, Procedure response) {
                    eventBus.fireEvent(new IncomeUpdatedEvent(response));
                }
            });
        } else {
            Alert.alert(ERR, EMPTY_FIELDS_ERR);
        }
    }
}
