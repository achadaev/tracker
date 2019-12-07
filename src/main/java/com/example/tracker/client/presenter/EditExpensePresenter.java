package com.example.tracker.client.presenter;

import com.example.tracker.client.event.ExpenseUpdatedEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

public class EditExpensePresenter implements Presenter {
    public interface Display {
        HasClickHandlers getSaveButton();
        HasValue<Integer> getId();
        HasValue<Integer> getTypeId();
        HasValue<String> getName();
        HasValue<String> getDate();
        HasValue<Integer> getPrice();
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
                EditExpensePresenter.this.display.getTypeId().setValue(expense.getTypeId());
                EditExpensePresenter.this.display.getName().setValue(expense.getName());
                EditExpensePresenter.this.display.getDate().setValue(expense.getDate());
                EditExpensePresenter.this.display.getPrice().setValue(expense.getPrice());            }
        });
    }

    public void bind() {
        this.display.getSaveButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                doSave();
            }
        });
    }

    public void go(HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
    }

    private void doSave() {
        expense.setId(display.getId().getValue());
        expense.setTypeId(display.getTypeId().getValue());
        expense.setName(display.getName().getValue());
        expense.setDate(display.getDate().getValue());
        expense.setPrice(display.getPrice().getValue());

        expenseWebService.updateExpense(expense, new MethodCallback<Expense>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error updating expense");
            }

            @Override
            public void onSuccess(Method method, Expense response) {
                eventBus.fireEvent(new ExpenseUpdatedEvent(response));
            }
        });
    }
}
