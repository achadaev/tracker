package com.example.tracker.client.presenter;

import com.example.tracker.client.event.ExpenseUpdatedEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;

public class EditExpensePresenter implements Presenter {
    public interface Display {
        HasClickHandlers getSaveButton();
        ListBox getTypeId();
        HasValue<String> getName();
//        DatePicker getDate();
        HasValue<String> getDate();
        HasValue<String> getPrice();
        Widget asWidget();
    }

    private Expense expense;
    private ExpenseWebService expenseWebService;
    private HandlerManager eventBus;
    private Display display;

    public EditExpensePresenter(ExpenseWebService expenseWebService, HandlerManager eventBus, Display display) {
        this.expenseWebService = expenseWebService;
        this.eventBus = eventBus;
        this.display = display;
        this.expense = new Expense();
        bind();
    }

    public EditExpensePresenter(ExpenseWebService expenseWebService, HandlerManager eventBus, Display display, int id) {
        this.expenseWebService = expenseWebService;
        this.eventBus = eventBus;
        this.display = display;
        bind();

        expenseWebService.getExpenseById(id, new MethodCallback<Expense>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error getting expense");
            }

            @Override
            public void onSuccess(Method method, Expense response) {
                expense = response;
                EditExpensePresenter.this.display.getTypeId().setItemSelected(expense.getTypeId() - 1, true);
                EditExpensePresenter.this.display.getName().setValue(expense.getName());
                EditExpensePresenter.this.display.getDate().setValue(expense.getDate());
                EditExpensePresenter.this.display.getPrice().setValue(Integer.toString(expense.getPrice()));
            }
        });
    }

    private void initTypesListBox(ListBox listBox) {
        expenseWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(Method method, List<ExpenseType> expenseTypes) {
                for (ExpenseType type : expenseTypes) {
                    listBox.addItem(type.getName(), Integer.toString(type.getId()));
                }
            }
        });
    }

    public void bind() {
        this.display.getSaveButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                doSave();
            }
        });
        initTypesListBox(this.display.getTypeId());
    }

    public void go(HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
    }

    private void doSave() {
        expense.setTypeId(Integer.parseInt(display.getTypeId().getSelectedValue()));
        expense.setName(display.getName().getValue());
//        expense.setDate(display.getDate().getHighlightedDate().toString());
        expense.setDate(display.getDate().getValue());
        expense.setPrice(Integer.parseInt(display.getPrice().getValue()));

        expenseWebService.updateExpense(expense, new MethodCallback<Expense>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert(exception.getMessage());
            }

            @Override
            public void onSuccess(Method method, Expense response) {
                eventBus.fireEvent(new ExpenseUpdatedEvent(response));
            }
        });
    }
}
