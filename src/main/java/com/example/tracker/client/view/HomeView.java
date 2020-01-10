package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.HomePresenter;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.MonthlyExpense;
import com.example.tracker.shared.model.SimpleDate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.googlecode.gwt.charts.client.corechart.AreaChartOptions;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.options.HAxis;
import com.googlecode.gwt.charts.client.options.VAxis;

import java.util.Date;
import java.util.List;

public class HomeView extends Composite implements HomePresenter.Display {
    interface HomeViewUiBinder extends UiBinder<HTMLPanel, HomeView> {
    }

    @UiField
    Label greetingLabel;
    @UiField
    HTMLPanel reviewPanel;
    @UiField
    Label amountLabel;
    @UiField
    Label monthLabel;
    @UiField
    Label weekLabel;
    @UiField
    Label moreLabel;

    private PieChart pieChart;
    private AreaChart areaChart;

    @Override
    public void initPieChart(List<Expense> expenseList) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(new Runnable() {
            @Override
            public void run() {
                pieChart = new PieChart();
                reviewPanel.add(pieChart);
                drawPieChart(expenseList);
            }
        });
    }

    @Override
    public void initAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(new Runnable() {
            @Override
            public void run() {
                areaChart = new AreaChart();
                reviewPanel.add(areaChart);
                drawAreaChart(dates, expenses);
            }
        });
    }

    private void drawAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses) {
        List<ExpenseType> types = ExpensesGWTController.getTypes();
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Date");

        for (ExpenseType type : types) {
            dataTable.addColumn(ColumnType.NUMBER, type.getName());
        }

        dataTable.addRows(dates.size());
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MMMM, yyyy");
        for (int i = 0; i < dates.size(); i++) {
            GWT.log(dates.get(i).toString());
            dataTable.setValue(i, 0, dateTimeFormat.format(dates.get(i).getDate()));
        }

        for (int row = 0; row < expenses.get(0).getExpenses().size(); row++) {
            for (int col = 0; col < expenses.size(); col++) {
                dataTable.setValue(col, row + 1, expenses.get(col).getExpenses().get(row).toString());
            }
        }

        areaChart.draw(dataTable);
        areaChart.setHeight("500px");
        areaChart.setWidth("700px");
    }

    private void drawPieChart(List<Expense> expenseList) {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Type");
        dataTable.addColumn(ColumnType.NUMBER, "Price");

        List<ExpenseType> types = ExpensesGWTController.getTypes();
        dataTable.addRows(types.size());
        int i = 0;
        int j = 0;
        for (ExpenseType type : types) {
            dataTable.setValue(i, j, type.getName());
            dataTable.setValue(i, j + 1, countByType(i + 1, expenseList));
            i++;
        }

        pieChart.draw(dataTable);
        pieChart.setWidth("400px");
        pieChart.setHeight("400px");
    }

    private double countByType(int typeId, List<Expense> expenseList) {
        double total = 0.0;
        for (Expense expense : expenseList) {
            if (expense.getTypeId() == typeId) {
                total += expense.getPrice();
            }
        }
        return total;
    }

    private static HomeViewUiBinder ourUiBinder = GWT.create(HomeViewUiBinder.class);

    public HomeView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Label getGreetingLabel() {
        return greetingLabel;
    }

    @Override
    public Label getAmountLabel() {
        return amountLabel;
    }

    @Override
    public Label getMonthLabel() {
        return monthLabel;
    }

    @Override
    public Label getWeekLabel() {
        return weekLabel;
    }

    @Override
    public Label getMoreLabel() {
        return moreLabel;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}