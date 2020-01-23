package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.message.AlertWidget;
import com.example.tracker.client.presenter.ExpensePresenter;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

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
    ListBox typesListBox;
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

    private CellTable<Expense> expenseTable;
    private ExpenseWebService expenseWebService;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    private MultiSelectionModel<Expense> selectionModel;

    public ExpenseView(ExpenseWebService expenseWebService) {
        initWidget(ourUiBinder.createAndBindUi(this));
        startDate.setVisible(false);
        endDate.setVisible(false);
        this.expenseWebService = expenseWebService;
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

        checkAllHeader.setUpdater(value -> {
            for (Expense expense : data) {
                selectionModel.setSelected(expense, value);
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

        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("d MMMM yyyy, EEEE");
        TextColumn<Expense> dateColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return dateTimeFormat.format(expense.getDate());
            }
        };
        expenseTable.addColumn(dateColumn, "Date");
        /*
        DateCell dateCell = new DateCell();
        Column<Expense, Date> dateColumn = new Column<Expense, Date>(dateCell) {
            @Override
            public Date getValue(Expense expense) {
                return expense.getDate();
            }
        };
        expenseTable.addColumn(dateColumn, "Date");
*/

        Column<Expense, Number> priceColumn = new Column<Expense, Number>(new NumberCell()) {
            @Override
            public Number getValue(Expense expense) {
                return expense.getPrice();
            }
        };
        priceColumn.setSortable(true);
        expenseTable.addColumn(priceColumn, "Price");

        if (ExpensesGWTController.isAdmin) {
            TextColumn<Expense> isArchivedColumn = new TextColumn<Expense>() {
                @Override
                public String getValue(Expense expense) {
                    return expense.getIsArchived() == 1 ? "yes" : "no";
                }
            };
            expenseTable.addColumn(isArchivedColumn, "Is Archived");
        }

        expenseTable.setPageSize(10);
        expenseTable.setRowData(0, data);
        SimplePager pager = new SimplePager();
        pager.setDisplay(expenseTable);

        expenseTable.getColumnSortList().push(new ColumnSortList.ColumnSortInfo(priceColumn, false));

        AsyncDataProvider<Expense> provider = new AsyncDataProvider<Expense>() {
            @Override
            protected void onRangeChanged(HasData<Expense> display)
            {
                final Range range = display.getVisibleRange();
                final ColumnSortList sortList = expenseTable.getColumnSortList();

                int start = range.getStart();
                int length = range.getLength();
                if (startDate.getValue() == null && endDate.getValue() == null) {
                    Date nullDate = new Date(0);
                    expenseWebService.getSortedAndFilteredExpenses(Integer.parseInt(typesListBox.getSelectedValue()),
                            nullDate, nullDate, start, length, sortList.get(0).isAscending(),
                            new MethodCallback<List<Expense>>() {
                                @Override
                                public void onFailure(Method method, Throwable throwable) {
                                    AlertWidget.alert("Error", "Error sorting expenses").center();
                                }

                                @Override
                                public void onSuccess(Method method, List<Expense> response) {
                                    updateRowData(start, response);
                                }
                            });
                } else {
                    expenseWebService.getSortedAndFilteredExpenses(Integer.parseInt(typesListBox.getSelectedValue()),
                            startDate.getValue(), endDate.getValue(), start, length, sortList.get(0).isAscending(),
                            new MethodCallback<List<Expense>>() {
                                @Override
                                public void onFailure(Method method, Throwable throwable) {
                                    AlertWidget.alert("Error", "Error sorting expenses").center();
                                }

                                @Override
                                public void onSuccess(Method method, List<Expense> response) {
                                    updateRowData(start, response);
                                }
                            });
                }
            }
        };
        provider.addDataDisplay(expenseTable);
        provider.updateRowCount(data.size(), true);
        ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(expenseTable);
        expenseTable.addColumnSortHandler(columnSortHandler);


        tablePanel.add(expenseTable);
        tablePanel.add(pager);
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
        return typesListBox;
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