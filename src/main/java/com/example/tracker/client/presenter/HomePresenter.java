package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.ShowExpensesEvent;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.widget.Alert;
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
        Heading getMonthChange();
        Heading getAmountLabel();
        Heading getMonthLabel();
        Heading getWeekLabel();
        Anchor getMoreAnchor();
        void initPieChart(Map<String, Double> data, List<ProcedureType> types, boolean isExpense);
        void initAreaChart(List<SimpleDate> dates, List<ProcedureType> types, List<MonthlyExpense> expenses);
        Widget asWidget();
    }

    private ProcedureWebService procedureWebService;
    private TypeWebService typeWebService;
    private HandlerManager eventBus;
    private Display display;

    private List<ProcedureType> expenseTypes;
    private List<ProcedureType> incomeTypes;

    public HomePresenter(ProcedureWebService procedureWebService, TypeWebService typeWebService,
                         HandlerManager eventBus, Display view) {
        this.procedureWebService = procedureWebService;
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = view;

        getExpenseTypes();
        getIncomeTypes();
    }

    private void getExpenseTypes() {
        typeWebService.getExpenseTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_TYPES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                expenseTypes = response;
            }
        });
    }

    private void getIncomeTypes() {
        typeWebService.getIncomeTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_TYPES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                incomeTypes = response;
            }
        });
    }

    public void bind() {
        display.getMoreAnchor().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowExpensesEvent()));
    }

    private void initExpensePieChart() {
        procedureWebService.getExpensesReviewByTypes(new MethodCallback<Map<String, Double>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_REVIEW_ERR);
            }

            @Override
            public void onSuccess(Method method, Map<String, Double> response) {
                display.initPieChart(response, expenseTypes,true);
            }
        });
    }

    private void initIncomePieChart() {
        procedureWebService.getIncomesReviewByTypes(new MethodCallback<Map<String, Double>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_REVIEW_ERR);
            }

            @Override
            public void onSuccess(Method method, Map<String, Double> response) {
                display.initPieChart(response, incomeTypes, false);
            }
        });
    }

    private void initAreaChart(List<SimpleDate> dates) {
        procedureWebService.getExpensesBetween(new MethodCallback<List<MonthlyExpense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_BETWEEN_EXPENSES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<MonthlyExpense> response) {
                display.initAreaChart(dates, expenseTypes, response);
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
                Alert.alert(ERR, GETTING_REVIEW_ERR);
            }

            @Override
            public void onSuccess(Method method, ReviewInfo response) {
                reviewInfo = response;
                display.getMonthChange().setText(Double.toString(reviewInfo.getMonthChange()));
                if (reviewInfo.getMonthChange() < 0) {
                    display.getMonthChange().setColor("red");
                } else {
                    display.getMonthChange().setColor("green");
                }
                display.getAmountLabel().setText(display.getAmountLabel().getText() + " " + reviewInfo.getAmount());
                display.getMonthLabel().setText(display.getMonthLabel().getText() + " " + reviewInfo.getMonth());
                display.getWeekLabel().setText(display.getWeekLabel().getText() + " " + reviewInfo.getWeek());
            }
        });

        initExpensePieChart();
        initIncomePieChart();

        procedureWebService.getDatesBetween(new MethodCallback<List<SimpleDate>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_BETWEEN_DATES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<SimpleDate> response) {
                initAreaChart(response);
            }
        });
    }
}
