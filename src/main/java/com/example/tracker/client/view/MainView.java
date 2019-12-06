package com.example.tracker.client.view;

import com.example.tracker.client.presenter.MainPresenter;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

import java.util.List;

public class MainView extends Composite implements MainPresenter.Display {
    interface MainViewUiBinder extends UiBinder<HTMLPanel, MainView> {
    }

    @UiField
    Button getAllExpensesButton;
    @UiField
    FlexTable expenseTable;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    public MainView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setData(List<Expense> data) {
        expenseTable.removeAllRows();

        for (int i = 0; i < data.size(); i++) {
            expenseTable.setText(i, 1, Integer.toString(data.get(i).getId()));
            expenseTable.setText(i, 2, Integer.toString(data.get(i).getType_id()));
            expenseTable.setText(i, 3, data.get(i).getName());
            expenseTable.setText(i, 4, data.get(i).getDate());
            expenseTable.setText(i, 5, Integer.toString(data.get(i).getPrice()));
        }
    }

    @Override
    public HasClickHandlers getAllExpensesButton() {
        return getAllExpensesButton;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}