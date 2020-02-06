package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.ShowFilteredExpensesEvent;
import com.example.tracker.client.event.incomes.ShowFilteredIncomesEvent;
import com.example.tracker.client.presenter.HomePresenter;
import com.example.tracker.shared.model.Procedure;
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

import java.util.List;

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
    Label greetingLabel;
    @UiField
    HTMLPanel reviewPanel;
    @UiField
    HorizontalPanel expenseChartPanel;
    @UiField
    HorizontalPanel incomeChartPanel;
    @UiField
    Label amountLabel;
    @UiField
    Label monthLabel;
    @UiField
    Label weekLabel;
    @UiField
    Label moreLabel;

    private HandlerManager eventBus;

    private PieChart expensePieChart;
    private PieChart incomePieChart;
    private AreaChart areaChart;

    DataTable expenseDataTable;
    DataTable incomeDataTable;

    @Override
    public void initPieChart(List<Procedure> procedureList, boolean isExpense) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(() -> {
            if (isExpense) {
                expensePieChart = new PieChart();
                expensePieChart.addSelectHandler(new SelectHandler() {
                    @Override
                    public void onSelect(SelectEvent selectEvent) {
                        JsArray<Selection> selection = expensePieChart.getSelection();
                        String type = expenseDataTable.getValueString(selection.get(0).getRow(), 0);
                        for (ProcedureType procedureType : ExpensesGWTController.getExpenseTypes()) {
                            if (type.equals(procedureType.getName())) {
                                eventBus.fireEvent(new ShowFilteredExpensesEvent(procedureType.getId()));
                            }
                        }
                    }
                });
                expenseChartPanel.add(expensePieChart);
                drawPieChart(procedureList, true);
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
                incomeChartPanel.add(incomePieChart);
                drawPieChart(procedureList, false);
            }
        });
    }

    private void drawPieChart(List<Procedure> procedureList, boolean isExpense) {
        List<ProcedureType> types;
        if (isExpense) {
            expenseDataTable = DataTable.create();
            expenseDataTable.addColumn(ColumnType.STRING, TYPE_COLUMN);
            expenseDataTable.addColumn(ColumnType.NUMBER, PRICE_COLUMN);

            types = ExpensesGWTController.getExpenseTypes();

            initDataTable(procedureList, types, expenseDataTable);

            expensePieChart.draw(expenseDataTable);
            expensePieChart.setWidth(PIE_CHART_HEIGHT);
            expensePieChart.setHeight(PIE_CHART_WIDTH);
        } else {
            incomeDataTable = DataTable.create();
            incomeDataTable.addColumn(ColumnType.STRING, TYPE_COLUMN);
            incomeDataTable.addColumn(ColumnType.NUMBER, PRICE_COLUMN);

            types = ExpensesGWTController.getIncomeTypes();

            initDataTable(procedureList, types, incomeDataTable);

            incomePieChart.draw(incomeDataTable);
            incomePieChart.setWidth(PIE_CHART_HEIGHT);
            incomePieChart.setHeight(PIE_CHART_WIDTH);
        }
    }

    private void initDataTable(List<Procedure> procedureList, List<ProcedureType> types, DataTable dataTable) {
        dataTable.addRows(types.size());
        int i = 0;
        int j = 0;
        for (ProcedureType type : types) {
            dataTable.setValue(i, j, type.getName());
            dataTable.setValue(i, j + 1, countByType(type.getId(), procedureList));
            i++;
        }
    }

    @Override
    public void initAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(() -> {
            areaChart = new AreaChart();
            expenseChartPanel.add(areaChart);
            drawAreaChart(dates, expenses);
        });
    }

    private void drawAreaChart(List<SimpleDate> dates, List<MonthlyExpense> expenses) {
        List<ProcedureType> expenseTypes = ExpensesGWTController.getExpenseTypes();
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, DATE_COLUMN);

        for (ProcedureType type : expenseTypes) {
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

    private double countByType(int typeId, List<Procedure> procedureList) {
        double total = 0.0;
        for (Procedure procedure : procedureList) {
            if (procedure.getTypeId() == typeId) {
                total += procedure.getPrice();
            }
        }
        return total;
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