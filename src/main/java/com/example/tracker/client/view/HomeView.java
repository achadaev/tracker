package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.ShowFilteredExpensesEvent;
import com.example.tracker.client.event.incomes.ShowFilteredIncomesEvent;
import com.example.tracker.client.presenter.HomePresenter;
import com.example.tracker.shared.model.ProcedureType;
import com.example.tracker.shared.model.MonthlyExpense;
import com.example.tracker.shared.model.SimpleDate;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.googlecode.gwt.charts.client.*;
import com.googlecode.gwt.charts.client.corechart.AreaChart;
import com.googlecode.gwt.charts.client.corechart.PieChart;
import com.googlecode.gwt.charts.client.event.SelectEvent;
import com.googlecode.gwt.charts.client.event.SelectHandler;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.extras.toggleswitch.client.ui.ToggleSwitch;

import java.util.List;
import java.util.Map;

import static com.example.tracker.client.constant.TableConstants.*;

public class HomeView extends Composite implements HomePresenter.Display {
    interface HomeViewUiBinder extends UiBinder<HTMLPanel, HomeView> {
    }

    private static HomeViewUiBinder ourUiBinder = GWT.create(HomeViewUiBinder.class);

    public HomeView(HandlerManager eventBus) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.eventBus = eventBus;
    }

    @UiField
    Heading greetingHeading;
    @UiField
    HorizontalPanel expenseChartPanel;
    @UiField
    HorizontalPanel incomeChartPanel;
    @UiField
    Heading monthChange;
    @UiField
    Heading amountLabel;
    @UiField
    Heading monthLabel;
    @UiField
    Heading weekLabel;
    @UiField
    Anchor moreAnchor;
    @UiField
    ToggleSwitch isOwn;

    private HandlerManager eventBus;

    private PieChart expensePieChart;
    private PieChart incomePieChart;
    private AreaChart areaChart;

    private DataTable expenseDataTable;
    private DataTable incomeDataTable;

    @Override
    public void initPieChart(Map<String, Double> data, List<ProcedureType> types, boolean isExpense) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(() -> {
            if (isExpense) {
                expensePieChart = new PieChart();
                expensePieChart.addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent selectEvent) {
                        JsArray<Selection> selection = expensePieChart.getSelection();
                        String type = expenseDataTable.getValueString(selection.get(0).getRow(), 0);
                        for (ProcedureType procedureType : types) {
                            if (type.equals(procedureType.getName())) {
                                eventBus.fireEvent(new ShowFilteredExpensesEvent(procedureType.getId()));
                            }
                        }
                    }
                });
                expenseChartPanel.clear();
                expenseChartPanel.add(expensePieChart);
                drawPieChart(data, true);
            } else {
                incomePieChart = new PieChart();
                incomePieChart.addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent selectEvent) {
                        JsArray<Selection> selection = incomePieChart.getSelection();
                        String type = incomeDataTable.getValueString(selection.get(0).getRow(), 0);
                        for (ProcedureType procedureType : ExpensesGWTController.getIncomeTypes()) {
                            if (type.equals(procedureType.getName())) {
                                eventBus.fireEvent(new ShowFilteredIncomesEvent(procedureType.getId()));
                            }
                        }
                    }
                });
                incomeChartPanel.clear();
                incomeChartPanel.add(incomePieChart);
                drawPieChart(data, false);
            }
        });
    }

    private void drawPieChart(Map<String, Double> data, boolean isExpense) {
        if (isExpense) {
            expenseDataTable = DataTable.create();
            expenseDataTable.addColumn(ColumnType.STRING, TYPE_COLUMN);
            expenseDataTable.addColumn(ColumnType.NUMBER, PRICE_COLUMN);

            initDataTable(data, expenseDataTable);

            expensePieChart.draw(expenseDataTable);
            expensePieChart.setWidth(PIE_CHART_HEIGHT);
            expensePieChart.setHeight(PIE_CHART_WIDTH);
        } else {
            incomeDataTable = DataTable.create();
            incomeDataTable.addColumn(ColumnType.STRING, TYPE_COLUMN);
            incomeDataTable.addColumn(ColumnType.NUMBER, PRICE_COLUMN);

            initDataTable(data, incomeDataTable);

            incomePieChart.draw(incomeDataTable);
            incomePieChart.setWidth(PIE_CHART_HEIGHT);
            incomePieChart.setHeight(PIE_CHART_WIDTH);
        }
    }

    private void initDataTable(Map<String, Double> data, DataTable dataTable) {
        dataTable.addRows(data.size());
        int i = 0;
        int j = 0;
        for (String name : data.keySet()) {
            dataTable.setValue(i, j, name);
            dataTable.setValue(i, j + 1, data.get(name));
            i++;
        }
    }

    @Override
    public void initAreaChart(List<SimpleDate> dates, List<ProcedureType> types, List<MonthlyExpense> expenses) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(() -> {
            areaChart = new AreaChart();
            expenseChartPanel.add(areaChart);
            drawAreaChart(dates, types, expenses);
        });
    }

    private void drawAreaChart(List<SimpleDate> dates, List<ProcedureType> types, List<MonthlyExpense> expenses) {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, DATE_COLUMN);

        for (ProcedureType type : types) {
            dataTable.addColumn(ColumnType.NUMBER, type.getName());
        }

        dataTable.addRows(dates.size());
        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(CHART_DATE_PATTERN);
        for (int i = 0; i < dates.size(); i++) {
            dataTable.setValue(i, 0, dateTimeFormat.format(dates.get(i).getDate()));
        }

        for (int row = 0; row < expenses.get(0).getExpenses().size(); row++) {
            for (int col = 0; col < expenses.size(); col++) {
                dataTable.setValue(col, row + 1, expenses.get(col).getExpenses().get(row).toString());
            }
        }

        areaChart.draw(dataTable);
        areaChart.setHeight(AREA_CHART_HEIGHT);
        areaChart.setWidth(AREA_CHART_WIDTH);
    }

    @Override
    public Heading getGreetingHeading() {
        return greetingHeading;
    }

    @Override
    public Heading getMonthChange() {
        return monthChange;
    }

    @Override
    public Heading getAmountLabel() {
        return amountLabel;
    }

    @Override
    public Heading getMonthLabel() {
        return monthLabel;
    }

    @Override
    public Heading getWeekLabel() {
        return weekLabel;
    }

    @Override
    public Anchor getMoreAnchor() {
        return moreAnchor;
    }

    @Override
    public ToggleSwitch getIsOwn() {
        return isOwn;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}