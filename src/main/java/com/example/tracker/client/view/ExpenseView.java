package com.example.tracker.client.view;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExpenseView extends Composite implements ExpensePresenter.Display {
    interface MainViewUiBinder extends UiBinder<HTMLPanel, ExpenseView> {
    }

    @UiField
    HTMLPanel tablePanel;
    @UiField
    Button expensesButton;
    @UiField
    Button addButton;
    @UiField
    Button profileButton;
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
    HTMLPanel profileBarPanel;

    private CellTable<Expense> expenseTable;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    private MultiSelectionModel<Expense> selectionModel;

    public ExpenseView() {
        initWidget(ourUiBinder.createAndBindUi(this));
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
    }

    @Override
    public HasClickHandlers getExpensesButton() {
        return expensesButton;
    }

    @Override
    public HasClickHandlers getAddButton() {
        return addButton;
    }

    @Override
    public HasClickHandlers getProfileButton() {
        return profileButton;
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
    public HTMLPanel getProfileBarPanel() {
        return profileBarPanel;
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
    public CheckBox dateCheckBox() {
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
    public Widget asWidget() {
        return this;
    }
}