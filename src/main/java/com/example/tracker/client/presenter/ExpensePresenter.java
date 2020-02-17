package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.AddExpenseEvent;
import com.example.tracker.client.event.expense.EditExpenseEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.widget.Alert;
import com.example.tracker.client.widget.Confirm;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.example.tracker.shared.model.SelectionValue;
import com.example.tracker.shared.model.User;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.extras.select.client.ui.Option;
import org.gwtbootstrap3.extras.select.client.ui.Select;

import java.util.ArrayList;
import java.util.List;

import static com.example.tracker.client.constant.WidgetConstants.*;

public class ExpensePresenter implements Presenter, Confirm.Confirmation {

    private List<Procedure> procedureList;
    private List<ProcedureType> expenseTypes;
    protected List<User> users;

    public interface Display {
        HasClickHandlers getAddButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        Select getTypeSelection();
        Select getUserSelection();
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

    public ExpensePresenter() {
    }

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            UserWebService userWebService, HandlerManager eventBus, Display view) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = view;

        initSelections();
    }

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            UserWebService userWebService, HandlerManager eventBus, Display view, int typeId) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = view;
        this.typeId = typeId;

        initSelections();
    }

    protected void initSelections() {
        procedureWebService.getSelectionValue(-1, new MethodCallback<SelectionValue>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_SELECTION_ERR);
            }

            @Override
            public void onSuccess(Method method, SelectionValue response) {
                expenseTypes = response.getTypes();
                initTypeSelection(display.getTypeSelection(), expenseTypes);
                display.getTypeSelection().refresh();
                if (ExpensesGWTController.isAdmin()) {
                    users = response.getUsers();
                    initUserSelection(display.getUserSelection(), users);
                    display.getUserSelection().refresh();
                }
            }
        });
    }

    protected void initTypeSelection(Select select, List<ProcedureType> procedureTypeList) {
        Option all = new Option();
        all.setContent("All Expenses");
        all.setValue("-100");
        select.add(all);

        for (ProcedureType type : procedureTypeList) {
            Option option = new Option();
            option.setContent(type.getName());
            option.setValue(Integer.toString(type.getId()));
            select.add(option);
        }
    }

    protected void initUserSelection(Select select, List<User> userList) {
        Option all = new Option();
        all.setContent("All Users");
        all.setValue("0");
        select.add(all);

        for (User user : userList) {
            Option option = new Option();
            option.setContent(user.getLogin());
            option.setValue(Integer.toString(user.getId()));
            select.add(option);
        }
    }

    public void bind() {
        display.getAddButton().addClickHandler(clickEvent -> eventBus.fireEvent(new AddExpenseEvent()));

        display.getEditButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() == 1) {
                eventBus.fireEvent(new EditExpenseEvent(selectedIds.get(0)));
            } else {
                Alert.alert(ERR, ONE_ROW_ERR);
            }
        });

        display.getDeleteButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() >= 1) {
                confirmDeleting();
            } else {
                Alert.alert(ERR, AT_LEAST_ONE_ROW_ERR);
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
    }

    protected void confirmDeleting() {
        Confirm.confirm(this, CONFIRMATION, DELETING_FIELDS_LABEL);
    }

    @Override
    public void onConfirm() {
        deleteSelectedIds();
    }

    protected void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        procedureWebService.archiveProcedures(selectedIds, new MethodCallback<List<Procedure>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Alert.alert(ERR, DELETING_PROCEDURES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<Procedure> response) {
                procedureList = response;
                display.setData(procedureList, expenseTypes);
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
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getProceduresByDate(typeId, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                Alert.alert(ERR, FILTERING_EXPENSES_BY_DATE_ERR);
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, expenseTypes);
                                updateTotal(display.getTotalLabel());
                            }
                        });
            } else {
            procedureWebService.getProceduresByTypeId(typeId, new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    Alert.alert(ERR, FILTERING_EXPENSES_ERR);
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;
                    display.setData(procedureList, expenseTypes);
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    protected void filterProcedures(int typeId, int userId) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                procedureWebService.getProceduresByDate(typeId, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        userId, new MethodCallback<List<Procedure>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                Alert.alert(ERR, FILTERING_EXPENSES_BY_DATE_ERR);
                            }

                            @Override
                            public void onSuccess(Method method, List<Procedure> response) {
                                procedureList = response;
                                display.setData(procedureList, expenseTypes);
                                updateTotal(display.getTotalLabel());
                            }
                });
        } else {
            procedureWebService.getProceduresByTypeId(typeId, userId, new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    Alert.alert(ERR, FILTERING_EXPENSES_ERR);
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;
                    display.setData(procedureList, expenseTypes);
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
                    Alert.alert(ERR, GETTING_EXPENSES_ERR);
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;

                    display.setData(procedureList, expenseTypes);
                    updateTotal(display.getTotalLabel());
                }
            });
        } else {
            procedureWebService.getUsersExpenses(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    Alert.alert(ERR, GETTING_EXPENSES_ERR);
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    procedureList = response;

                    display.setData(procedureList, expenseTypes);
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
            display.getUserSelection().setVisible(false);
        }

        if (typeId != 0) {
            filterProcedures(typeId);
        } else {
            setProcedureTableData();
        }
    }
}
