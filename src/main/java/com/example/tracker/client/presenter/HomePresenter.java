package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.ShowExpensesEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.Date;
import java.util.List;

public class HomePresenter implements Presenter {

    private List<Expense> expenseList;

    public interface Display {
        Label getGreetingLabel();
        Panel getReviewPanel();
        Label getMoreLabel();
        Widget asWidget();
    }

    private ExpenseWebService expenseWebService;
    private HandlerManager eventBus;
    private Display display;

    public HomePresenter(ExpenseWebService expenseWebService, HandlerManager eventBus, Display view) {
        this.expenseWebService = expenseWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    public void bind() {
        display.getMoreLabel().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new ShowExpensesEvent());
            }
        });
    }

    private double getTotal(List<Expense> list) {
        double total = 0.0;
        for (Expense expense : list) {
            total += expense.getPrice();
        }
        return total;
    }

    private void setAmountPrice() {
        expenseWebService.getUsersExpenses(new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting amount expenses");
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
                double amount = getTotal(expenseList);
                display.getReviewPanel().add(new Label("Total expenses: " + amount));
            }
        });
    }

    private void setMonthPrice() {
        Date monthBefore = new Date();
        CalendarUtil.addMonthsToDate(monthBefore, -1);
        expenseWebService.getExpensesByDate(0, monthBefore, new Date(), new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting month expenses");
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
                double month = getTotal(expenseList);
                display.getReviewPanel().add(new Label("This month expenses: " + month));
            }
        });
    }

    private void setWeekPrice() {
        Date weekBefore = new Date();
        CalendarUtil.addDaysToDate(weekBefore, -7);
        expenseWebService.getExpensesByDate(0, weekBefore, new Date(), new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting week expenses");
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
                double week = getTotal(expenseList);
                display.getReviewPanel().add(new Label("This week expenses: " + week));
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        display.getGreetingLabel().setText("Hello, " + ExpensesGWTController.getUser().getLogin());
        setAmountPrice();
        setMonthPrice();
        setWeekPrice();
    }
}
