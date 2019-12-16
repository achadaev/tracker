package com.example.tracker.client.view;

import com.example.tracker.client.presenter.ExpensePresenter;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

public class ExpenseView extends Composite implements ExpensePresenter.Display {
    interface MainViewUiBinder extends UiBinder<HTMLPanel, ExpenseView> {
    }

    @UiField
    Button getAllExpensesButton;
    @UiField
    Button addButton;
    @UiField
    Button profileButton;
    @UiField
    Button deleteButton;
    @UiField
    FlexTable expenseTable;
    @UiField
    HTMLPanel profileBarPanel;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    public ExpenseView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        expenseTable.setVisible(false);
    }

    @Override
    public void setData(List<Expense> data) {
        expenseTable.setVisible(true);
        expenseTable.removeAllRows();
        expenseTable.setText(0, 0, "ID");
        expenseTable.setText(0, 1, "Type");
        expenseTable.setText(0, 2, "Name");
        expenseTable.setText(0, 3, "Date");
        expenseTable.setText(0, 4, "Price");
        expenseTable.setText(0, 5, "");
        expenseTable.getRowFormatter().addStyleName(0, "expenseTableHeader");
        expenseTable.getCellFormatter().addStyleName(0, 4, "expenseTablePriceColumn");
        expenseTable.getCellFormatter().addStyleName(0, 3, "expenseTableDateColumn");
        expenseTable.getCellFormatter().addStyleName(0, 2, "expenseTableNameColumn");

        for (int i = 0; i < data.size(); i++) {
            expenseTable.setText(i + 1, 0, Integer.toString(data.get(i).getId()));
            expenseTable.setText(i + 1, 1, Integer.toString(data.get(i).getTypeId()));
            expenseTable.setText(i + 1, 2, data.get(i).getName());
            expenseTable.setText(i + 1, 3, data.get(i).getDate());
            expenseTable.setText(i + 1, 4, Integer.toString(data.get(i).getPrice()));
            expenseTable.setWidget(i + 1, 5, new CheckBox());


            if (i % 2 == 0) {
                expenseTable.getRowFormatter().addStyleName(i, "evenRow");
            }
            expenseTable.getCellFormatter().addStyleName(i + 1, 4, "expenseTablePriceColumn");
            expenseTable.getCellFormatter().addStyleName(i + 1, 3, "expenseTableDateColumn");
            expenseTable.getCellFormatter().addStyleName(i + 1, 2, "expenseTableNameColumn");
        }
    }

    @Override
    public HasClickHandlers getAllExpensesButton() {
        return getAllExpensesButton;
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
    public HasClickHandlers getDeleteButton() {
        return deleteButton;
    }

    @Override
    public HTMLPanel getProfileBarPanel() {
        return profileBarPanel;
    }

    @Override
    public List<Integer> getSelectedRows() {
        List<Integer> selectedRows = new ArrayList<>();

        for (int i = 1; i < expenseTable.getRowCount(); i++) {
            CheckBox checkBox = (CheckBox) expenseTable.getWidget(i, 5);
            if (checkBox.getValue()) {
                selectedRows.add(i);
            }
        }

        return selectedRows;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}