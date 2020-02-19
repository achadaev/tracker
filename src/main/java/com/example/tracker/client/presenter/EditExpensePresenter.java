package com.example.tracker.client.presenter;

import com.example.tracker.client.event.expense.ExpenseUpdatedEvent;
import com.example.tracker.client.widget.Alert;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.Currency;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.List;
import java.util.Map;

import static com.example.tracker.client.constant.WidgetConstants.*;

public class EditExpensePresenter implements Presenter {

    public interface Display {
        HasClickHandlers getSaveButton();
        HasClickHandlers getCancelButton();
        Select getTypeId();
        HasValue<String> getName();
        DatePicker getDate();
        Select getCurrency();
        HasValue<String> getPrice();
        Widget asWidget();
        void show();
        void hide();
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

        initTypesSelection(display.getTypeId());
        initCurrencySelection(display.getCurrency());
    }

    public EditExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                                HandlerManager eventBus, Display display, int id) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;

        procedureWebService.getProcedureById(id, new MethodCallback<Procedure>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Alert.alert(ERR, GETTING_PROCEDURE_ERR);
            }

            @Override
            public void onSuccess(Method method, Procedure response) {
                procedure = response;
                for (int i = 0; i < display.getTypeId().getItemCount(); i++) {
                    if (procedure.getTypeId() == Integer.parseInt(display.getTypeId().getItem(i).getValue())) {
                        display.getTypeId().setValue(display.getTypeId().getItem(i).getValue());
                    }
                }
                display.getName().setValue(procedure.getName());
                display.getDate().setValue(procedure.getDate());
                display.getPrice().setValue(Double.toString(procedure.getPrice()));
            }
        });

        initTypesSelection(display.getTypeId());
        initCurrencySelection(display.getCurrency());
    }

    protected void initTypesSelection(Select select) {
        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
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

    protected void initCurrencySelection(Select select) {
        procedureWebService.getCurrency(new MethodCallback<Currency>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_CURRENCY_ERR);
            }

            @Override
            public void onSuccess(Method method, Currency response) {
                if (response == null) {
                    Option option = new Option();
                    option.setContent("RUB");
                    option.setValue("1");
                    select.add(option);
                    select.refresh();
                    select.setEnabled(false);
                } else {
                    setCurrency(select, response);
                }
            }
        });
    }

    protected void setTypes(Select select, List<ProcedureType> types) {
        for (ProcedureType type : types) {
            Option option = new Option();
            option.setContent(type.getName());
            option.setValue(Integer.toString(type.getId()));
            select.add(option);
        }
        select.refresh();
    }

    protected void setCurrency(Select select, Currency currency) {
        for (Map.Entry<String, Double> entry : currency.getRates().entrySet()) {
            Option option = new Option();
            option.setContent(entry.getKey());
            option.setValue(Double.toString(entry.getValue()));
            select.add(option);
        }
        select.refresh();
    }

    public void bind() {
        this.display.getSaveButton().addClickHandler(clickEvent -> doSave());
        this.display.getCancelButton().addClickHandler(clickEvent -> display.hide());
    }

    public void go(HasWidgets container) {
        bind();
        display.show();
    }

    protected double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            Alert.alert(ERR, INCORRECT_PRICE_ERR);
        }
        return 0.0;
    }

    protected void doSave() {
        if (!"".equals(display.getName().getValue())
                && display.getDate().getValue() != null
                && !"".equals(display.getPrice().getValue())) {
            if (toDouble(display.getPrice().getValue()) != 0.0) {
                procedure.setTypeId(Integer.parseInt(display.getTypeId().getValue()));
                procedure.setKind(-1);
                procedure.setName(display.getName().getValue());
                procedure.setDate(display.getDate().getValue());
                procedure.setPrice(Double.parseDouble(display.getPrice().getValue())
                            / Double.parseDouble(display.getCurrency().getValue()));

                procedureWebService.updateProcedure(procedure, new MethodCallback<Procedure>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Alert.alert(ERR, UPDATING_EXPENSE_ERR);
                    }

                    @Override
                    public void onSuccess(Method method, Procedure response) {
                        eventBus.fireEvent(new ExpenseUpdatedEvent(response));
                    }
                });
                display.hide();
            }
        } else {
            Alert.alert(ERR, EMPTY_FIELDS_ERR);
        }
    }
}
