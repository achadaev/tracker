package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.ShowExpensesEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.*;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class HomePresenter implements Presenter {

    private ReviewInfo reviewInfo;

    public interface Display {
        Label getGreetingLabel();
        Label getAmountLabel();
        Label getMonthLabel();
        Label getWeekLabel();
        Label getMoreLabel();
        void initPieChart(List<Procedure> procedureList);
        void initAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses);
        Widget asWidget();
    }

    private ProcedureWebService procedureWebService;
    private HandlerManager eventBus;
    private Display display;

    public HomePresenter(ProcedureWebService procedureWebService, HandlerManager eventBus, Display view) {
        this.procedureWebService = procedureWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    public void bind() {
        display.getMoreLabel().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowExpensesEvent()));
    }

    private void initPieChart() {
        if (ExpensesGWTController.isAdmin()) {
            procedureWebService.getAllExpenses(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error getting expenses").center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    display.initPieChart(response);
                }
            });
        } else {
            procedureWebService.getUsersExpenses(new MethodCallback<List<Procedure>>() {
                @Override
                public void onFailure(Method method, Throwable throwable) {
                    AlertWidget.alert("Error", "Error getting expenses").center();
                }

                @Override
                public void onSuccess(Method method, List<Procedure> response) {
                    display.initPieChart(response);
                }
            });
        }
    }

    private void initAreaChart(List<SimpleDate> dates) {
        procedureWebService.getExpensesBetween(new MethodCallback<List<MonthlyExpense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting expenses between").center();
            }

            @Override
            public void onSuccess(Method method, List<MonthlyExpense> response) {
                display.initAreaChart(dates, response);
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        display.getGreetingLabel().setText("Hello, " + ExpensesGWTController.getUser().getLogin());
        procedureWebService.getReview(new MethodCallback<ReviewInfo>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting review").center();
            }

            @Override
            public void onSuccess(Method method, ReviewInfo response) {
                reviewInfo = response;
                display.getAmountLabel().setText(display.getAmountLabel().getText() + reviewInfo.getAmount());
                display.getMonthLabel().setText(display.getMonthLabel().getText() + reviewInfo.getMonth());
                display.getWeekLabel().setText(display.getWeekLabel().getText() + reviewInfo.getWeek());
            }
        });

        initPieChart();

        procedureWebService.getDatesBetween(new MethodCallback<List<SimpleDate>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", throwable.getMessage()).center();
            }

            @Override
            public void onSuccess(Method method, List<SimpleDate> response) {
                initAreaChart(response);
            }
        });
    }
}
