package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.ShowExpensesEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.User;
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
import java.util.Map;

public class HomePresenter implements Presenter {

    private List<Expense> expenseList;

    public interface Display {
        Label getGreetingLabel();
        Panel getReviewPanel();
        Label getAmountLabel();
        Label getMonthLabel();
        Label getWeekLabel();
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

    private double getTotal(List<Expense> expenseList) {
        double total = 0.0;
        for (Expense expense : expenseList) {
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
                display.getAmountLabel().setText(display.getAmountLabel().getText() + getTotal(expenseList));
/*
                expenseWebService.getTotal(expenseList, new MethodCallback<Map<String, Double>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Window.alert("Error getting total");
                    }

                    @Override
                    public void onSuccess(Method method, Map<String, Double> response) {
                        display.getReviewPanel().add(new Label("Total expenses: " + response.get("response")));
                    }
                });
*/
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
                display.getMonthLabel().setText(display.getMonthLabel().getText() + getTotal(expenseList));
/*
                expenseWebService.getTotal(expenseList, new MethodCallback<Map<String, Double>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Window.alert("Error getting total");
                    }

                    @Override
                    public void onSuccess(Method method, Map<String, Double> response) {
                        display.getReviewPanel().add(new Label("This month expenses: " + response.get("response")));
                    }
                });
*/
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
                display.getWeekLabel().setText(display.getWeekLabel().getText() + getTotal(expenseList));
/*
                expenseWebService.getTotal(expenseList, new MethodCallback<Map<String, Double>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Window.alert("Error getting total");
                    }

                    @Override
                    public void onSuccess(Method method, Map<String, Double> response) {
                        display.getReviewPanel().add(new Label("This week expenses: " + response.get("response")));
                    }
                });
*/
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
