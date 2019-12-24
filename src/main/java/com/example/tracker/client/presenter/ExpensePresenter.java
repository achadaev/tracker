package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.AddExpenseEvent;
import com.example.tracker.client.event.EditExpenseEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.ProfileBarView;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.apache.tapestry.wml.Do;
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
    private HandlerManager eventBus;
    private Display display;


    public ExpensePresenter(ExpenseWebService expenseWebService, HandlerManager eventBus, Display view) {
        this.expenseWebService = expenseWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    private void initTypesListBox(ListBox listBox) {
        expenseWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert(throwable.getMessage());
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

        display.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new AddExpenseEvent());
            }
        });

        display.getEditButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                List<Integer> selectedIds = display.getSelectedIds();

                if (selectedIds.size() == 1) {
                    eventBus.fireEvent(new EditExpenseEvent(selectedIds.get(0)));
                } else {
                    Window.alert("Select one row");
                }
            }
        });

        display.getDeleteButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                deleteSelectedIds();
            }
        });

        display.getFilerButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                filterExpenses(Integer.parseInt(display.getTypesListBox().getSelectedValue()));
            }
        });

        display.getDateCheckBox().addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> valueChangeEvent) {
                if (display.getDateCheckBox().getValue()) {
                    display.getStartDate().setVisible(true);
                    display.getEndDate().setVisible(true);
                } else {
                    display.getStartDate().setVisible(false);
                    display.getEndDate().setVisible(false);
                }
            }
        });

        initTypesListBox(this.display.getTypesListBox());
    }

    private void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        expenseWebService.deleteExpenses(selectedIds, new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error deleting expenses");
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
                                Window.alert("Error filtering expenses by date");
                            }

                            @Override
                            public void onSuccess(Method method, List<Expense> response) {
                                expenseList = response;
                                display.setData(expenseList, ExpensesGWTController.getTypes());
                                updateTotal(display.getTotalLabel());
                            }
                        });
            } else {
                Window.alert("Select dates");
            }
        } else {
            expenseWebService.getExpensesByTypeId(id, new MethodCallback<List<Expense>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    Window.alert("Error filtering expenses");
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
        expenseWebService.getUsersExpenses(new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert(exception.getMessage());
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;

                display.setData(expenseList, ExpensesGWTController.getTypes());
                updateTotal(display.getTotalLabel());
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        setExpenseTableData();
    }
}
