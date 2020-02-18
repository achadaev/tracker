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
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitch;

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
        ToggleSwitch getIsOwn();
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

        if (!ExpensesGWTController.isAdmin()) {
            display.getIsOwn().removeFromParent();
        }
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

        display.getIsOwn().addValueChangeHandler(valueChangeEvent -> {
           initExpensePieChart();
           initIncomePieChart();
           initAreaChart();
           getReview(valueChangeEvent.getValue());
        });
    }

    private void initExpensePieChart() {
        if (display.getIsOwn() != null) {
            getExpensesReviewByTypes(display.getIsOwn().getValue());
        } else {
            getExpensesReviewByTypes(true);
        }
    }

    private void getExpensesReviewByTypes(boolean isOwn) {
        procedureWebService.getExpensesReviewByTypes(isOwn, new MethodCallback<Map<String, Double>>() {
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
        if (display.getIsOwn() != null) {
            getIncomesReviewByTypes(display.getIsOwn().getValue());
        } else {
            getIncomesReviewByTypes(true);
        }
    }

    private void getIncomesReviewByTypes(boolean isOwn) {
        procedureWebService.getIncomesReviewByTypes(isOwn, new MethodCallback<Map<String, Double>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_REVIEW_ERR);
            }

            @Override
            public void onSuccess(Method method, Map<String, Double> response) {
                display.initPieChart(response, incomeTypes,false);
            }
        });
    }

    private void createAreaChart(List<SimpleDate> dates, boolean isOwn) {
        procedureWebService.getExpensesBetween(isOwn, new MethodCallback<List<MonthlyExpense>>() {
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

    private void initReview() {
        if (display.getIsOwn() != null) {
            getReview(display.getIsOwn().getValue());
        } else {
            getReview(true);
        }
    }

    private void getReview(boolean isOwn) {
        procedureWebService.getReview(isOwn, new MethodCallback<ReviewInfo>() {
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
                display.getAmountLabel().setText(TOTAL_EXPENSES_LABEL + reviewInfo.getAmount());
                display.getMonthLabel().setText(THIS_MONTH_EXPENSES_LABEL + reviewInfo.getMonth());
                display.getWeekLabel().setText(THIS_WEEK_EXPENSES_LABEL + reviewInfo.getWeek());
            }
        });
    }

    private void initAreaChart() {
        procedureWebService.getDatesBetween(new MethodCallback<List<SimpleDate>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Alert.alert(ERR, GETTING_BETWEEN_DATES_ERR);
            }

            @Override
            public void onSuccess(Method method, List<SimpleDate> response) {
                if (display.getIsOwn() != null) {
                    createAreaChart(response, display.getIsOwn().getValue());
                } else {
                    createAreaChart(response, true);
                }
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        display.getGreetingHeading().setText("Hello, " + ExpensesGWTController.getUser().getLogin());

        initExpensePieChart();
        initIncomePieChart();
        initAreaChart();
        initReview();

    }
}
