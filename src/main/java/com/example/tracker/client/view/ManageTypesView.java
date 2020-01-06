package com.example.tracker.client.view;

import com.example.tracker.client.presenter.ManageTypesPresenter;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

import java.util.ArrayList;
import java.util.List;

public class ManageTypesView extends Composite implements ManageTypesPresenter.Display {
    interface ManageTypesViewUiBinder extends UiBinder<HTMLPanel, ManageTypesView> {
    }

    @UiField
    HTMLPanel tablePanel;
    @UiField
    Button addButton;
    @UiField
    Button editButton;
    @UiField
    Button deleteButton;

    private CellTable<ExpenseType> typeTable;

    private MultiSelectionModel<ExpenseType> selectionModel;

    private static ManageTypesViewUiBinder ourUiBinder = GWT.create(ManageTypesViewUiBinder.class);

    public ManageTypesView() {
        initWidget(ourUiBinder.createAndBindUi(this));
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

        for (ExpenseType type : selectionModel.getSelectedSet()) {
            selectedRows.add(type.getId());
        }

        return selectedRows;
    }

    @Override
    public void setData(List<ExpenseType> typeList) {
        typeTable = new CellTable<>();
        typeTable.setVisible(true);
        selectionModel = new MultiSelectionModel<>();
        typeTable.setSelectionModel(selectionModel);
        tablePanel.clear();

        CheckboxCell checkboxCell = new CheckboxCell();

        Column<ExpenseType, Boolean> checkColumn = new Column<ExpenseType, Boolean>(checkboxCell) {
            @Override
            public Boolean getValue(ExpenseType type) {
                return selectionModel.isSelected(type);
            }
        };

        CheckboxCell checkAllHeaderCB = new CheckboxCell(true, true);
        Header<Boolean> checkAllHeader = new Header<Boolean>(checkAllHeaderCB) {
            @Override
            public Boolean getValue() {
                return selectionModel.getSelectedSet().size() == typeList.size();
            }
        };

        checkAllHeader.setUpdater(new ValueUpdater<Boolean>() {
            @Override
            public void update(Boolean value) {
                for (ExpenseType type: typeList) {
                    selectionModel.setSelected(type, value);
                }
            }
        });
        typeTable.addColumn(checkColumn, checkAllHeader);

        Column<ExpenseType, Number> idColumn = new Column<ExpenseType, Number>(new NumberCell()) {
            @Override
            public Number getValue(ExpenseType type) {
                return type.getId();
            }
        };
        typeTable.addColumn(idColumn, "ID");

        TextColumn<ExpenseType> typeColumn = new TextColumn<ExpenseType>() {
            @Override
            public String getValue(ExpenseType type) {
                return type.getName();
            }
        };
        typeTable.addColumn(typeColumn, "Name");

        typeTable.setPageSize(10);
        typeTable.setRowData(0, typeList);
        SimplePager pager = new SimplePager();
        pager.setDisplay(typeTable);

        AsyncDataProvider<ExpenseType> provider = new AsyncDataProvider<ExpenseType>()
        {
            @Override
            protected void onRangeChanged(HasData<ExpenseType> display)
            {
                int start = display.getVisibleRange().getStart();
                int end = start + display.getVisibleRange().getLength();
                end = Math.min(end, typeList.size());
                List<ExpenseType> sub = typeList.subList(start, end);
                updateRowData(start, sub);
            }
        };
        provider.addDataDisplay(typeTable);
        provider.updateRowCount(typeList.size(), true);

        tablePanel.add(typeTable);
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}