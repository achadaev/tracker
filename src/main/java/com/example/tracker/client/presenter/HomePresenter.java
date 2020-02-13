package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.ShowExpensesEvent;
import com.example.tracker.client.widget.AlertWidget;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.*;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Heading;

import java.util.List;
import java.util.Map;

import static com.example.tracker.client.constant.WidgetConstants.*;

public class HomePresenter implements Presenter {

    private ReviewInfo reviewInfo;

    public interface Display {
        Heading getGreetingHeading();
        Heading getAmountLabel();
        Heading getMonthLabel();
        Heading getWeekLabel();
        Anchor getMoreAnchor();
        void initPieChart(Map<String, Double> data, boolean isExpense);
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
        display.getMoreAnchor().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowExpensesEvent()));
    }

    private void initExpensePieChart() {
        procedureWebService.getExpensesReviewByTypes(new MethodCallback<Map<String, Double>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_REVIEW_ERR).center();
            }

            @Override
            public void onSuccess(Method method, Map<String, Double> response) {
                display.initPieChart(response, true);
            }
        });
    }

    private void initIncomePieChart() {
        procedureWebService.getIncomesReviewByTypes(new MethodCallback<Map<String, Double>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_REVIEW_ERR).center();
            }

            @Override
            public void onSuccess(Method method, Map<String, Double> response) {
                display.initPieChart(response, false);
            }
        });
    }

    private void initAreaChart(List<SimpleDate> dates) {
        procedureWebService.getExpensesBetween(new MethodCallback<List<MonthlyExpense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_BETWEEN_EXPENSES_ERR).center();
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
        display.getGreetingHeading().setText("Hello, " + ExpensesGWTController.getUser().getLogin());
        procedureWebService.getReview(new MethodCallback<ReviewInfo>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_REVIEW_ERR).center();
            }

            @Override
            public void onSuccess(Method method, ReviewInfo response) {
                reviewInfo = response;
                display.getAmountLabel().setText(display.getAmountLabel().getText() + reviewInfo.getAmount());
                display.getMonthLabel().setText(display.getMonthLabel().getText() + reviewInfo.getMonth());
                display.getWeekLabel().setText(display.getWeekLabel().getText() + reviewInfo.getWeek());
            }
        });

        initExpensePieChart();
        initIncomePieChart();

        procedureWebService.getDatesBetween(new MethodCallback<List<SimpleDate>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert(ERR, GETTING_BETWEEN_DATES_ERR).center();
            }

            @Override
            public void onSuccess(Method method, List<SimpleDate> response) {
                initAreaChart(response);
            }
        });
    }
}
