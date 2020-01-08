package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.ExpensePresenter;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.corechart.PieChart;

import java.util.*;

public class ExpenseView extends Composite implements ExpensePresenter.Display {
    interface MainViewUiBinder extends UiBinder<HTMLPanel, ExpenseView> {
    }

    @UiField
    HTMLPanel tablePanel;
    @UiField
    Button addButton;
    @UiField
    Button editButton;
    @UiField
    Button deleteButton;
    @UiField
    ListBox types;
    @UiField
    CheckBox dateCheckBox;
    @UiField
    DatePicker startDate;
    @UiField
    DatePicker endDate;
    @UiField
    Button filter;
    @UiField
    Label total;

    private PieChart chart;

    private CellTable<Expense> expenseTable;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    private MultiSelectionModel<Expense> selectionModel;

    public ExpenseView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        startDate.setVisible(false);
        endDate.setVisible(false);
    }

    private void initPieChart(List<Expense> expenseList) {
        ChartLoader chartLoader = new ChartLoader(ChartPackage.CORECHART);
        chartLoader.loadApi(new Runnable() {
            @Override
            public void run() {
                chart = new PieChart();
                tablePanel.add(chart);
                drawPieChart(expenseList);
            }
        });
    }

    private void drawPieChart(List<Expense> expenseList) {
        DataTable dataTable = DataTable.create();
        dataTable.addColumn(ColumnType.STRING, "Type");
        dataTable.addColumn(ColumnType.NUMBER, "Price");

        List<ExpenseType> types = ExpensesGWTController.getTypes();
        List<Double> totalByType = new ArrayList<>();
        dataTable.addRows(types.size());
        int i = 0;
        int j = 0;
        for (ExpenseType type : types) {
            dataTable.setValue(i, j, type.getName());
            dataTable.setValue(i, j + 1, countByType(i + 1, expenseList));
            i++;
        }

        chart.draw(dataTable);
        chart.setWidth("400px");
        chart.setHeight("400px");
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

    @Override
    public void setData(List<Expense> data, List<ExpenseType> types) {
        tablePanel.clear();
        expenseTable = new CellTable<>();
        expenseTable.setVisible(true);
        selectionModel = new MultiSelectionModel<>();
        expenseTable.setSelectionModel(selectionModel);

        CheckboxCell checkboxCell = new CheckboxCell();

        Column<Expense, Boolean> checkColumn = new Column<Expense, Boolean>(checkboxCell) {
            @Override
            public Boolean getValue(Expense expense) {
                return selectionModel.isSelected(expense);
            }
        };

        CheckboxCell checkAllHeaderCB = new CheckboxCell(true, true);
        Header<Boolean> checkAllHeader = new Header<Boolean>(checkAllHeaderCB) {
            @Override
            public Boolean getValue() {
                return selectionModel.getSelectedSet().size() == data.size();
            }
        };

        checkAllHeader.setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                for (Expense expense : data) {
                    selectionModel.setSelected(expense, value);
                }
            }
        });
        expenseTable.addColumn(checkColumn, checkAllHeader);

        Column<Expense, Number> idColumn = new Column<Expense, Number>(new NumberCell()) {
            @Override
            public Number getValue(Expense expense) {
                return expense.getId();
            }
        };
        expenseTable.addColumn(idColumn, "ID");

        TextColumn<Expense> typeColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                for (ExpenseType type : types) {
                    if (type.getId() == expense.getTypeId()) {
                        return type.getName();
                    }
                }
                return "undefined";
            }
        };
        expenseTable.addColumn(typeColumn, "Type");

        TextColumn<Expense> nameColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return expense.getName();
            }
        };
        expenseTable.addColumn(nameColumn, "Name");

        DateCell dateCell = new DateCell();
        Column<Expense, Date> dateColumn = new Column<Expense, Date>(dateCell) {
            @Override
            public Date getValue(Expense expense) {
                return expense.getDate();
            }
        };
        expenseTable.addColumn(dateColumn, "Date");

        Column<Expense, Number> priceColumn = new Column<Expense, Number>(new NumberCell()) {
            @Override
            public Number getValue(Expense expense) {
                return expense.getPrice();
            }
        };
        expenseTable.addColumn(priceColumn, "Price");

        expenseTable.setPageSize(10);
        expenseTable.setRowData(0, data);
        SimplePager pager = new SimplePager();
        pager.setDisplay(expenseTable);

        AsyncDataProvider<Expense> provider = new AsyncDataProvider<Expense>()
        {
            @Override
            protected void onRangeChanged(HasData<Expense> display)
            {
                int start = display.getVisibleRange().getStart();
                int end = start + display.getVisibleRange().getLength();
                end = Math.min(end, data.size());
                List<Expense> sub = data.subList(start, end);
                updateRowData(start, sub);
            }
        };
        provider.addDataDisplay(expenseTable);
        provider.updateRowCount(data.size(), true);

        tablePanel.add(expenseTable);

        initPieChart(data);
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addButton;
    }

    @Override
    public HasClickHandlers getEditButton() {
        return editButton;
    }

    @Override
    public HasClickHandlers getDeleteButton() {
        return deleteButton;
    }

    @Override
    public List<Integer> getSelectedIds() {
        List<Integer> selectedRows = new ArrayList<>();

        for (Expense expense : selectionModel.getSelectedSet()) {
            selectedRows.add(expense.getId());
        }

        return selectedRows;
    }

    @Override
    public ListBox getTypesListBox() {
        return types;
    }

    @Override
    public CheckBox getDateCheckBox() {
        return dateCheckBox;
    }

    @Override
    public DatePicker getStartDate() {
        return startDate;
    }

    @Override
    public DatePicker getEndDate() {
        return endDate;
    }

    @Override
    public HasClickHandlers getFilerButton() {
        return filter;
    }

    @Override
    public Label getTotalLabel() {
        return total;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}