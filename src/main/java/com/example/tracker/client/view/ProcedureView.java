package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.widget.Alert;
import com.example.tracker.client.presenter.ExpensePresenter;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.Range;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;

import java.util.*;

import static com.example.tracker.client.constant.TableConstants.*;
import static com.example.tracker.client.constant.WidgetConstants.*;

public class ProcedureView extends Composite implements ExpensePresenter.Display {
    interface MainViewUiBinder extends UiBinder<HTMLPanel, ProcedureView> {
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
    Select typeSelection;
    @UiField
    Select userSelection;
    @UiField
    DatePicker startDate;
    @UiField
    DatePicker endDate;
    @UiField
    Button filter;
    @UiField
    Label total;

    private CellTable<Procedure> procedureCellTable;
    private ProcedureWebService procedureWebService;
    private MultiSelectionModel<Procedure> selectionModel;

    private int typeId = 0;
    private int start;
    private int length;
    private String columnName = "ID";
    private boolean isAscending;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    public ProcedureView(ProcedureWebService procedureWebService) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.procedureWebService = procedureWebService;
        startDate.setValue(null);
        endDate.setValue(null);
    }

    public ProcedureView(ProcedureWebService procedureWebService, int typeId) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.procedureWebService = procedureWebService;
        this.typeId = typeId;
        startDate.setValue(null);
        endDate.setValue(null);
    }

    @Override
    public void setData(List<Procedure> data, List<ProcedureType> types) {
        tablePanel.clear();
        procedureCellTable = new CellTable<>();
        procedureCellTable.setVisible(true);
        selectionModel = new MultiSelectionModel<>();
        procedureCellTable.setSelectionModel(selectionModel);

        CheckboxCell checkboxCell = new CheckboxCell();

        Column<Procedure, Boolean> checkColumn = new Column<Procedure, Boolean>(checkboxCell) {
            @Override
            public Boolean getValue(Procedure procedure) {
                return selectionModel.isSelected(procedure);
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
            for (Procedure procedure : data) {
                selectionModel.setSelected(procedure, value);
            }
        });
        procedureCellTable.addColumn(checkColumn, checkAllHeader);

        Column<Procedure, Number> idColumn = new Column<Procedure, Number>(new NumberCell()) {
            @Override
            public Number getValue(Procedure procedure) {
                return procedure.getId();
            }
        };
        idColumn.setSortable(true);
        idColumn.setDataStoreName(ID_COLUMN);
        procedureCellTable.addColumn(idColumn, ID_COLUMN);

        if (ExpensesGWTController.isAdmin()) {
            TextColumn<Procedure> usernameColumn = new TextColumn<Procedure>() {
                @Override
                public String getValue(Procedure procedure) {
                    return procedure.getUsername();
                }
            };
            usernameColumn.setSortable(true);
            usernameColumn.setDataStoreName(USERNAME_COLUMN);
            procedureCellTable.addColumn(usernameColumn, USERNAME_COLUMN);
        }

        TextColumn<Procedure> typeColumn = new TextColumn<Procedure>() {
            @Override
            public String getValue(Procedure procedure) {
                for (ProcedureType type : types) {
                    if (type.getId() == procedure.getTypeId()) {
                        return type.getName();
                    }
                }
                return UNDEFINED_VALUE;
            }
        };
        typeColumn.setSortable(true);
        typeColumn.setDataStoreName(TYPE_COLUMN);
        procedureCellTable.addColumn(typeColumn, TYPE_COLUMN);

        TextColumn<Procedure> nameColumn = new TextColumn<Procedure>() {
            @Override
            public String getValue(Procedure procedure) {
                return procedure.getName();
            }
        };
        nameColumn.setSortable(true);
        nameColumn.setDataStoreName(NAME_COLUMN);
        procedureCellTable.addColumn(nameColumn, NAME_COLUMN);

        DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(TABLE_DATE_PATTERN);
        TextColumn<Procedure> dateColumn = new TextColumn<Procedure>() {
            @Override
            public String getValue(Procedure procedure) {
                return dateTimeFormat.format(procedure.getDate());
            }
        };
        dateColumn.setSortable(true);
        dateColumn.setDataStoreName(DATE_COLUMN);
        procedureCellTable.addColumn(dateColumn, DATE_COLUMN);

        Column<Procedure, Number> priceColumn = new Column<Procedure, Number>(new NumberCell()) {
            @Override
            public Number getValue(Procedure procedure) {
                return procedure.getPrice();
            }
        };
        priceColumn.setSortable(true);
        priceColumn.setDataStoreName(PRICE_COLUMN);
        procedureCellTable.addColumn(priceColumn, PRICE_COLUMN);

        if (ExpensesGWTController.isAdmin()) {
            TextColumn<Procedure> isArchivedColumn = new TextColumn<Procedure>() {
                @Override
                public String getValue(Procedure procedure) {
                    return procedure.getIsArchived() == 1 ? YES_VALUE : NO_VALUE;
                }
            };
            procedureCellTable.addColumn(isArchivedColumn, IS_ARCHIVED_COLUMN);
        }

        procedureCellTable.setPageSize(10);
        procedureCellTable.setRowData(0, data);
        SimplePager pager = new SimplePager();
        pager.setDisplay(procedureCellTable);

        procedureCellTable.getColumnSortList().push(new ColumnSortList.ColumnSortInfo(priceColumn, false));

        AsyncDataProvider<Procedure> provider = new AsyncDataProvider<Procedure>() {
            @Override
            protected void onRangeChanged(HasData<Procedure> display) {
                final Range range = display.getVisibleRange();
                start = range.getStart();
                length = range.getLength();
                show(this);
            }
        };

        provider.addDataDisplay(procedureCellTable);
        provider.updateRowCount(data.size(), false);

        procedureCellTable.addColumnSortHandler(columnSortEvent -> {
            isAscending = columnSortEvent.isSortAscending();
            columnName = columnSortEvent.getColumn().getDataStoreName();
            show(provider);
        });

        tablePanel.add(procedureCellTable);
        tablePanel.add(pager);
    }

    private void show(AsyncDataProvider<Procedure> provider) {
        if (ExpensesGWTController.isAdmin()) {
            if (startDate.getValue() == null && endDate.getValue() == null) {
                Date nullDate = new Date(0);
                if (typeId != 0) {
                    typeSelection.setValue(Integer.toString(typeId));
                    doSort(provider, typeId, nullDate, nullDate, start, length, columnName, isAscending, 0);
                    typeId = 0;
                } else {
                    doSort(provider, Integer.parseInt(typeSelection.getValue()), nullDate, nullDate, start,
                            length, columnName, isAscending, Integer.parseInt(userSelection.getValue()));
                }
            } else {
                doSort(provider, Integer.parseInt(typeSelection.getValue()),
                        startDate.getValue(), endDate.getValue(), start, length, columnName, isAscending,
                        Integer.parseInt(userSelection.getValue()));
            }
        } else {
            if (startDate.getValue() == null && endDate.getValue() == null) {
                Date nullDate = new Date(0);
                if (typeId != 0) {
                    typeSelection.setValue(Integer.toString(typeId));
                    doSort(provider, typeId, nullDate, nullDate, start, length, columnName, isAscending);
                    typeId = 0;
                } else {
                    doSort(provider, Integer.parseInt(typeSelection.getValue()), nullDate, nullDate, start, length,
                            columnName, isAscending);
                }
            } else {
                doSort(provider, Integer.parseInt(typeSelection.getValue()),
                        startDate.getValue(), endDate.getValue(), start, length, columnName, isAscending);
            }
        }
    }

    private void doSort(AsyncDataProvider<Procedure> provider, int typeId, Date startDate, Date endDate, int startIndex,
                        int quantity, String column, boolean isAscending) {
        procedureWebService.getSortedAndFilteredProcedures(typeId, startDate, endDate, startIndex, quantity,
                column, isAscending, new MethodCallback<List<Procedure>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Alert.alert(ERR, SORTING_PROCEDURES_ERR);
                    }

                    @Override
                    public void onSuccess(Method method, List<Procedure> response) {
                        provider.updateRowData(startIndex, response);
                    }
        });
    }

    private void doSort(AsyncDataProvider<Procedure> provider, int typeId, Date startDate, Date endDate, int startIndex,
                        int quantity, String column, boolean isAscending, int userId) {
        procedureWebService.getSortedAndFilteredProcedures(typeId, startDate, endDate, startIndex, quantity,
                column, isAscending, userId, new MethodCallback<List<Procedure>>() {
                    @Override
                    public void onFailure(Method method, Throwable throwable) {
                        Alert.alert(ERR, SORTING_PROCEDURES_ERR);
                    }

                    @Override
                    public void onSuccess(Method method, List<Procedure> response) {
                        provider.updateRowData(startIndex, response);
                    }
        });
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
        for (Procedure procedure : selectionModel.getSelectedSet()) {
            selectedRows.add(procedure.getId());
        }
        return selectedRows;
    }

    @Override
    public Select getTypeSelection() {
        return typeSelection;
    }

    @Override
    public Select getUserSelection() {
        return userSelection;
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