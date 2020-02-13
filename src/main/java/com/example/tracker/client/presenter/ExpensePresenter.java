package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.AddExpenseEvent;
import com.example.tracker.client.event.expense.EditExpenseEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.widget.ConfirmWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.example.tracker.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.List;

import static com.example.tracker.client.constant.WidgetConstants.*;

public class ExpensePresenter implements Presenter, ConfirmWidget.Confirmation {

    private List<Procedure> procedureList;

    public interface Display {
        HasClickHandlers getAddButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        Select getTypeSelection();
        ListBox getUsersListBox();
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
    protected UserWebService userWebService;
    protected HandlerManager eventBus;
    protected Display display;
    private int typeId = 0;

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            UserWebService userWebService, HandlerManager eventBus, Display view) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            UserWebService userWebService, HandlerManager eventBus, Display view, int typeId) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = view;
        this.typeId = typeId;
    }

    protected void initTypesListBox(ListBox listBox) {
        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_TYPES_ERR).center();
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

    protected void initTypeSelection(Select select) {
        Option f = new Option();
        f.setContent("First");
        f.setValue("1");
        Option s = new Option();
        s.setContent("Second");
        s.setValue("2");
        Option t = new Option();
        t.setContent("Third");
        t.setValue("3");
        select.add(f);
        select.add(s);
        select.add(t);
    }

    protected void initUsersListBox(ListBox listBox) {
        userWebService.getAllUsers(new MethodCallback<List<User>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_USERS_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<User> users) {
                listBox.addItem("All Users", "0");
                for (User user : users) {
                    listBox.addItem(user.getLogin(), Integer.toString(user.getId()));
                }
                listBox.setItemSelected(0, true);
            }
        });
    }

    public void bind() {
        initTypesListBox(display.getTypesListBox());
        initTypeSelection(display.getTypeSelection());
        if (ExpensesGWTController.isAdmin()) {
            initUsersListBox(display.getUsersListBox());
        }

        display.getAddButton().addClickHandler(clickEvent -> eventBus.fireEvent(new AddExpenseEvent()));

        display.getEditButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() == 1) {
                eventBus.fireEvent(new EditExpenseEvent(selectedIds.get(0)));
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
                filterProcedures(Integer.parseInt(display.getTypesListBox().getSelectedValue()),
                        Integer.parseInt(display.getUsersListBox().getSelectedValue()));
            } else {
                filterProcedures(Integer.parseInt(display.getTypesListBox().getSelectedValue()));
            }
        });

        display.getTypeSelection().addValueChangeHandler(valueChangeEvent -> {
            GWT.log("valueChangeEvent.getValue: " + valueChangeEvent.getValue());
            GWT.log("getTypeSelection().getValue: " + display.getTypeSelection().getValue());
            GWT.log("getTypeSelection().getSelectedItem: " + display.getTypeSelection().getSelectedItem());
            GWT.log("getTypeSelection().getSelectedItem().getValue: " + display.getTypeSelection().getSelectedItem().getValue());
            GWT.log("getTypeSelection().getSelectedItem().getContent: " + display.getTypeSelection().getSelectedItem().getContent());
        });

        display.getTypesListBox().addChangeHandler(changeEvent -> {
            if (ExpensesGWTController.isAdmin()) {
                filterProcedures(Integer.parseInt(display.getTypesListBox().getSelectedValue()),
                        Integer.parseInt(display.getUsersListBox().getSelectedValue()));
            } else {
                filterProcedures(Integer.parseInt(display.getTypesListBox().getSelectedValue()));
            }
        });

        if (ExpensesGWTController.isAdmin()) {
            display.getUsersListBox().addChangeHandler(changeEvent ->
                    filterProcedures(Integer.parseInt(display.getTypesListBox().getSelectedValue()),
                    Integer.parseInt(display.getUsersListBox().getSelectedValue())));
        }

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

    protected void confirmDeleting() {
        ConfirmWidget confirmWidget = new ConfirmWidget(this);
        confirmWidget.confirm(CONFIRMATION, DELETING_FIELDS_LABEL).center();
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
                AlertWidget.alert(ERR, DELETING_PROCEDURES_ERR).center();
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

    protected void filterProcedures(int typeId) {
        if (display.getDateCheckBox().getValue()) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getProceduresByDate(typeId, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                AlertWidget.alert(ERR, FILTERING_EXPENSES_BY_DATE_ERR).center();
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
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
                    AlertWidget.alert(ERR, FILTERING_EXPENSES_ERR).center();
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

    protected void filterProcedures(int typeId, int userId) {
        if (display.getDateCheckBox().getValue()) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getProceduresByDate(typeId, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        userId, new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                AlertWidget.alert(ERR, FILTERING_EXPENSES_BY_DATE_ERR).center();
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, ExpensesGWTController.getExpenseTypes());
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
                    AlertWidget.alert(ERR, FILTERING_EXPENSES_ERR).center();
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
        if (ExpensesGWTController.isAdmin()) {
            procedureWebService.getAllExpenses(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert(ERR, GETTING_EXPENSES_ERR).center();
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
                    AlertWidget.alert(ERR, GETTING_EXPENSES_ERR).center();
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

        if (!ExpensesGWTController.isAdmin()) {
            display.getUsersListBox().setVisible(false);
        }

        if (typeId != 0) {
            filterProcedures(typeId);
        } else {
            setProcedureTableData();
        }
    }
}
