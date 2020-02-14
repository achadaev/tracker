package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.incomes.AddIncomeEvent;
import com.example.tracker.client.event.incomes.EditIncomeEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Label;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.List;

import static com.example.tracker.client.constant.WidgetConstants.*;

public class IncomePresenter extends ExpensePresenter {

    private List<Procedure> procedureList;

    public IncomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService, UserWebService userWebService,
                           HandlerManager eventBus, Display view) {
        super(procedureWebService, typeWebService, userWebService, eventBus, view);
    }

    public IncomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService, UserWebService userWebService,
                           HandlerManager eventBus, Display view, int typeId) {
        super(procedureWebService, typeWebService, userWebService, eventBus, view, typeId);
    }

    @Override
    protected void initTypeSelection(Select select, List<ProcedureType> procedureTypeList) {
        Option all = new Option();
        all.setContent("All Incomes");
        all.setValue("100");
        select.add(all);

        for (ProcedureType type : procedureTypeList) {
            Option option = new Option();
            option.setContent(type.getName());
            option.setValue(Integer.toString(type.getId()));
            select.add(option);
        }
    }
/*
    @Override
    protected void initTypeSelection(Select select) {
        typeWebService.getIncomeTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_TYPES_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                Option all = new Option();
                all.setContent("All Incomes");
                all.setContent("100");
                select.add(all);

                for (ProcedureType type : response) {
                    Option option = new Option();
                    option.setContent(type.getName());
                    option.setValue(Integer.toString(type.getId()));
                    select.add(option);
                }
            }
        });
    }
*/

    @Override
    public void bind() {
        display.getTypeSelection().addValueChangeHandler(valueChangeEvent -> {
            if (ExpensesGWTController.isAdmin()) {
                filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()),
                        Integer.parseInt(display.getUserSelection().getValue()));
            } else {
                filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()));
            }
        });

        if (ExpensesGWTController.isAdmin()) {
            display.getUserSelection().addValueChangeHandler(valueChangeEvent ->
                    filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()),
                            Integer.parseInt(display.getUserSelection().getValue())));
        }

        display.getAddButton().addClickHandler(clickEvent -> eventBus.fireEvent(new AddIncomeEvent()));

        display.getEditButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() == 1) {
                eventBus.fireEvent(new EditIncomeEvent(selectedIds.get(0)));
            } else {
                AlertWidget.alert(ERR, ONE_ROW_ERR).center();
            }
        });

        display.getDeleteButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() >= 1) {
                confirmDeleting();
            } else {
                AlertWidget.alert(ERR, AT_LEAST_ONE_ROW_ERR).center();
            }
        });

        display.getFilerButton().addClickHandler(clickEvent -> {
            if (ExpensesGWTController.isAdmin()) {
                filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()),
                        Integer.parseInt(display.getUserSelection().getValue()));
            } else {
                filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()));
            }
        });

        display.getDateCheckBox().addValueChangeHandler(valueChangeEvent -> {
            if (display.getDateCheckBox().getValue()) {
                display.getStartDate().setVisible(true);
                display.getEndDate().setVisible(true);
            } else {
                display.getStartDate().setVisible(false);
                display.getEndDate().setVisible(false);
            }
        });
    }

    @Override
    protected void filterProcedures(int typeId) {
        if (display.getDateCheckBox().getValue()) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getProceduresByDate(typeId, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                AlertWidget.alert(ERR, FILTERING_INCOMES_BY_DATE_ERR).center();
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, ExpensesGWTController.getIncomeTypes());
                                updateTotal(display.getTotalLabel());
                            }
                        });
            } else {
                AlertWidget.alert(ERR, NULL_DATES_ERR).center();
            }
        } else {
            procedureWebService.getProceduresByTypeId(typeId, new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert(ERR, FILTERING_INCOMES_ERR).center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;
                    display.setData(procedureList, ExpensesGWTController.getIncomeTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    protected void filterProcedures(int typeId, int userId) {
        if (display.getDateCheckBox().getValue()) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getProceduresByDate(typeId, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        userId, new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                AlertWidget.alert(ERR, FILTERING_INCOMES_BY_DATE_ERR).center();
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, ExpensesGWTController.getIncomeTypes());
                                updateTotal(display.getTotalLabel());
                            }
                        });
            } else {
                AlertWidget.alert(ERR, NULL_DATES_ERR).center();
            }
        } else {
            procedureWebService.getProceduresByTypeId(typeId, userId, new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert(ERR, FILTERING_INCOMES_ERR).center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;
                    display.setData(procedureList, ExpensesGWTController.getIncomeTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    @Override
    protected void setProcedureTableData() {
        if (ExpensesGWTController.isAdmin()) {
            procedureWebService.getAllIncomes(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert(ERR, GETTING_INCOMES_ERR).center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;

                    display.setData(procedureList, ExpensesGWTController.getIncomeTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        } else {
            procedureWebService.getUsersIncomes(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    AlertWidget.alert(ERR, GETTING_INCOMES_ERR).center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;

                    display.setData(procedureList, ExpensesGWTController.getIncomeTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    @Override
    protected void updateTotal(Label label) {
        double total = 0.0;
        for (Procedure procedure : procedureList) {
            total += procedure.getPrice();
        }
        label.setText(Double.toString(total));
    }
}
