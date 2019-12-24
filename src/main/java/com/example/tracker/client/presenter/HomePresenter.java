package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
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
        void setReviewData(double amount, double month, double week);
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
    }

    private double getTotal() {
        double total = 0.0;
        for (Expense expense : expenseList) {
            total += expense.getPrice();
        }
        return total;
    }

    private double getAmountPrice() {
        expenseWebService.getUsersExpenses(new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting amount expenses");
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
            }
        });
        return getTotal();
    }

    private double getMonthPrice() {
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
            }
        });
        return getTotal();
    }

    private double getWeekPrice() {
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
            }
        });
        return getTotal();
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        display.getGreetingLabel().setText("Hello, " + ExpensesGWTController.getUser().getLogin());
        display.setReviewData(getAmountPrice(), getMonthPrice(), getWeekPrice());
    }
}
