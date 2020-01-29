package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.HomePresenter;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
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
import com.googlecode.gwt.charts.client.corechart.PieChart;

import java.util.List;

public class HomeView extends Composite implements HomePresenter.Display {
    interface HomeViewUiBinder extends UiBinder<HTMLPanel, HomeView> {
    }

    @UiField
    Label greetingLabel;
    @UiField
    HTMLPanel reviewPanel;
    @UiField
    HorizontalPanel chartPanel;
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
    public void initPieChart(List<Procedure> procedureList) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(() -> {
            pieChart = new PieChart();
            chartPanel.add(pieChart);
            drawPieChart(procedureList);
        });
    }

    @Override
    public void initAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(() -> {
            areaChart = new AreaChart();
            chartPanel.add(areaChart);
            drawAreaChart(dates, expenses);
        });
    }

    private void drawAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses) {
        List<ProcedureType> expenseTypes = ExpensesGWTController.getExpenseTypes();
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Date");

        for (ProcedureType type : expenseTypes) {
            dataTable.addColumn(ColumnType.NUMBER, type.getName());
        }

        dataTable.addRows(dates.size());
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("MMMM, yyyy");
        for (int i = 0; i < dates.size(); i++) {
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

    private void drawPieChart(List<Procedure> procedureList) {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Type");
        dataTable.addColumn(ColumnType.NUMBER, "Price");

        List<ProcedureType> expenseTypes = ExpensesGWTController.getExpenseTypes();
        dataTable.addRows(expenseTypes.size());
        int i = 0;
        int j = 0;
        for (ProcedureType type : expenseTypes) {
            dataTable.setValue(i, j, type.getName());
            dataTable.setValue(i, j + 1, countByType(i + 1, procedureList));
            i++;
        }

        pieChart.draw(dataTable);
        pieChart.setWidth("400px");
        pieChart.setHeight("400px");
    }

    private double countByType(int typeId, List<Procedure> procedureList) {
        double total = 0.0;
        for (Procedure procedure : procedureList) {
            if (procedure.getTypeId() == typeId) {
                total += procedure.getPrice();
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