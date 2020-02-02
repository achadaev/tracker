package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.AddExpenseEvent;
import com.example.tracker.client.event.expense.EditExpenseEvent;
import com.example.tracker.client.view.HomeView;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.widget.ConfirmWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensePresenter implements Presenter, ConfirmWidget.Confirmation {

    private List<Procedure> procedureList;

    public interface Display {
        HasClickHandlers getAddButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        ListBox getTypesListBox();
        CheckBox getDateCheckBox();
        DatePicker getStartDate();
        DatePicker getEndDate();
        HasClickHandlers getFilerButton();
        List<Integer> getSelectedIds();
        void setData(List<Procedure> data, List<ProcedureType> types);
        Label getTotalLabel();
        Widget asWidget();
    }

    protected ProcedureWebService procedureWebService;
    protected TypeWebService typeWebService;
    protected HandlerManager eventBus;
    protected Display display;
    private int typeId = 0;

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            HandlerManager eventBus, Display view) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            HandlerManager eventBus, Display view, int typeId) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = view;
        this.typeId = typeId;
    }

    protected void initTypesListBox(ListBox listBox) {
        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", throwable.getMessage()).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> procedureTypes) {
                listBox.addItem("All Expenses", "-100");
                for (ProcedureType type : procedureTypes) {
                    listBox.addItem(type.getName(), Integer.toString(type.getId()));
                }
            }
        });
    }

    public void bind() {
        display.getAddButton().addClickHandler(clickEvent -> eventBus.fireEvent(new AddExpenseEvent()));

        display.getEditButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() == 1) {
                eventBus.fireEvent(new EditExpenseEvent(selectedIds.get(0)));
            } else {
                AlertWidget.alert("Error", "Select one row").center();
            }
        });

        display.getDeleteButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() >= 1) {
                confirmDeleting();
            } else {
                AlertWidget.alert("Error", "Select at least one row").center();
            }
        });

        display.getFilerButton().addClickHandler(clickEvent -> filterProcedures(Integer.parseInt(display.getTypesListBox().getSelectedValue())));

        display.getDateCheckBox().addValueChangeHandler(valueChangeEvent -> {
            if (display.getDateCheckBox().getValue()) {
                display.getStartDate().setVisible(true);
                display.getEndDate().setVisible(true);
            } else {
                display.getStartDate().setVisible(false);
                display.getEndDate().setVisible(false);
            }
        });

        initTypesListBox(display.getTypesListBox());
    }

    protected void confirmDeleting() {
        ConfirmWidget confirmWidget = new ConfirmWidget(this);
        confirmWidget.confirm("Confirmation", "Do you actually want to delete these fields?").center();
    }

    @Override
    public void onConfirm() {
        deleteSelectedIds();
    }

    protected void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        procedureWebService.archiveProcedure(selectedIds, new MethodCallback<List<Procedure>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                AlertWidget.alert("Error", "Error deleting expenses").center();
            }

            @Override
            public void onSuccess(Method method, List<Procedure> response) {
                procedureList = response;
                display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
                updateTotal(display.getTotalLabel());
            }
        });
    }

    protected void updateTotal(Label label) {
        double total = 0.0;
        for (Procedure procedure : procedureList) {
            total += procedure.getPrice();
        }
        label.setText(Double.toString(total));
    }

    protected void filterProcedures(int id) {
        if (display.getDateCheckBox().getValue()) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getExpensesByDate(id, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                AlertWidget.alert("Error", "Error filtering expenses by date").center();
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
                                updateTotal(display.getTotalLabel());
                            }
                        });
            } else {
                AlertWidget.alert("Error", "Select dates").center();
            }
        } else {
            procedureWebService.getProceduresByTypeId(id, new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error filtering expenses").center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;
                    display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    protected void setProcedureTableData() {
        if (ExpensesGWTController.isAdmin) {
            procedureWebService.getAllExpenses(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error getting all expenses").center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;

                    display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        } else {
            procedureWebService.getUsersExpenses(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    AlertWidget.alert("Error", exception.getMessage()).center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;

                    display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());

        if (typeId != 0) {
            filterProcedures(typeId);
        } else {
            setProcedureTableData();
        }
    }
}
