package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.AddExpenseEvent;
import com.example.tracker.client.event.expense.EditExpenseEvent;
import com.example.tracker.client.message.AlertWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensePresenter implements Presenter {

    private List<Expense> expenseList;

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
        void setData(List<Expense> data, List<ExpenseType> types);
        Label getTotalLabel();
        Widget asWidget();
    }

    private ExpenseWebService expenseWebService;
    private TypeWebService typeWebService;
    private HandlerManager eventBus;
    private Display display;


    public ExpensePresenter(ExpenseWebService expenseWebService, TypeWebService typeWebService,
                            HandlerManager eventBus, Display view) {
        this.expenseWebService = expenseWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    private void initTypesListBox(ListBox listBox) {
        typeWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", throwable.getMessage()).center();
            }

            @Override
            public void onSuccess(Method method, List<ExpenseType> expenseTypes) {
                listBox.addItem("All", "0");
                for (ExpenseType type : expenseTypes) {
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

        display.getDeleteButton().addClickHandler(clickEvent -> deleteSelectedIds());

        display.getFilerButton().addClickHandler(clickEvent -> filterExpenses(Integer.parseInt(display.getTypesListBox().getSelectedValue())));

        display.getDateCheckBox().addValueChangeHandler(valueChangeEvent -> {
            if (display.getDateCheckBox().getValue()) {
                display.getStartDate().setVisible(true);
                display.getEndDate().setVisible(true);
            } else {
                display.getStartDate().setVisible(false);
                display.getEndDate().setVisible(false);
            }
        });

        initTypesListBox(this.display.getTypesListBox());
    }

    private void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        expenseWebService.archiveExpenses(selectedIds, new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                AlertWidget.alert("Error", "Error deleting expenses").center();
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
                display.setData(expenseList, ExpensesGWTController.getTypes());
                updateTotal(display.getTotalLabel());
            }
        });
    }

    private void updateTotal(Label label) {
        double total = 0.0;
        for (Expense expense : expenseList) {
            total += expense.getPrice();
        }
        label.setText(Double.toString(total));
    }

    private void filterExpenses(int id) {
        if (display.getDateCheckBox().getValue()) {
            if (display.getStartDate().getValue() != null && display.getEndDate().getValue() != null) {
                expenseWebService.getExpensesByDate(id, display.getStartDate().getValue(), display.getEndDate().getValue(),
                        new MethodCallback<List<Expense>>() {
                            @Override
                            public void onFailure(Method method, Throwable throwable) {
                                AlertWidget.alert("Error", "Error filtering expenses by date").center();
                            }

                            @Override
                            public void onSuccess(Method method, List<Expense> response) {
                                expenseList = response;
                                display.setData(expenseList, ExpensesGWTController.getTypes());
                                updateTotal(display.getTotalLabel());
                            }
                        });
            } else {
                AlertWidget.alert("Error", "Select dates").center();
            }
        } else {
            expenseWebService.getExpensesByTypeId(id, new MethodCallback<List<Expense>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error filtering expenses").center();
                }

                @Override
                public void onSuccess(Method method, List<Expense> response) {
                    expenseList = response;
                    display.setData(expenseList, ExpensesGWTController.getTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        }
    }

    private void setExpenseTableData() {
        if (ExpensesGWTController.isAdmin) {
            expenseWebService.getAllExpenses(new MethodCallback<List<Expense>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error getting all expenses").center();
                }

                @Override
                public void onSuccess(Method method, List<Expense> response) {
                    expenseList = response;

                    display.setData(expenseList, ExpensesGWTController.getTypes());
                    updateTotal(display.getTotalLabel());
                }
            });
        } else {
            expenseWebService.getUsersExpenses(new MethodCallback<List<Expense>>() {
                @Override
                public void onFailure(Method method, Throwable exception) {
                    AlertWidget.alert("Error", exception.getMessage()).center();
                }

                @Override
                public void onSuccess(Method method, List<Expense> response) {
                    expenseList = response;

                    display.setData(expenseList, ExpensesGWTController.getTypes());
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
        setExpenseTableData();
    }
}
