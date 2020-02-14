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
        Select getUserSelection();
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

        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_TYPES_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                initTypeSelection(display.getTypeSelection(), response);
            }
        });

        if (ExpensesGWTController.isAdmin()) {
            userWebService.getAllUsers(new MethodCallback<List<User>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert(ERR, GETTING_USERS_ERR).center();
                }

                @Override
                public void onSuccess(Method method, List<User> response) {
                    initUserSelection(display.getUserSelection(), response);
                }
            });
        }
    }

    public ExpensePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                            UserWebService userWebService, HandlerManager eventBus, Display view, int typeId) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = view;
        this.typeId = typeId;

        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_TYPES_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                initTypeSelection(display.getTypeSelection(), response);
            }
        });

        if (ExpensesGWTController.isAdmin()) {
            userWebService.getAllUsers(new MethodCallback<List<User>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert(ERR, GETTING_USERS_ERR).center();
                }

                @Override
                public void onSuccess(Method method, List<User> response) {
                    initUserSelection(display.getUserSelection(), response);
                }
            });
        }
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
        all.setContent("0");
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

        display.getTypeSelection().addValueChangeHandler(valueChangeEvent -> {
            if (ExpensesGWTController.isAdmin()) {
                filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()),
                        Integer.parseInt(display.getUserSelection().getValue()));
            } else {
                filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()));
            }
        });
        // HERE!
        if (ExpensesGWTController.isAdmin()) {
            display.getUserSelection().addValueChangeHandler(valueChangeEvent ->
                    filterProcedures(Integer.parseInt(display.getTypeSelection().getValue()),
                            Integer.parseInt(display.getUserSelection().getValue())));
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

                    //TODO change to types request
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
/*
        initTypeSelection(display.getTypeSelection());
        if (ExpensesGWTController.isAdmin()) {
            initUserSelection(display.getUserSelection());
        }
*/

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
