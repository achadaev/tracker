package com.example.tracker.client;

import com.example.tracker.client.view.mainPage.MainPageView;
import com.example.tracker.server.dao.Expense;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;

import java.util.*;

public class ExpensesGWTApplication implements EntryPoint {
    @Override
    public void onModuleLoad() {
        RootPanel.get().add(new MainPageView());
/*
        CellTable<Expense> expenseTable = new CellTable<>();

        TextColumn<Expense> idColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return Integer.toString(expense.getId());
            }
        };

        List<Expense> expenses = new ArrayList<Expense>();
        Expense x1 = new Expense();
        x1.setId(1);
        x1.setType_id(1);
        x1.setName("bus");
        x1.setDate("20.11.2019");
        x1.setPrice(100);
        Expense x2 = new Expense();
        x2.setId(1);
        x2.setType_id(2);
        x2.setName("taxi");
        x2.setDate("19.11.2019");
        x2.setPrice(200);
        expenses.add(x1);
        expenses.add(x2);

        TextColumn<Expense> typeIdColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return Integer.toString(expense.getType_id());
            }
        };
        TextColumn<Expense> nameColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return expense.getName();
            }
        };
        TextColumn<Expense> dateColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return expense.getDate();
            }
        };
        TextColumn<Expense> priceColumn = new TextColumn<Expense>() {
            @Override
            public String getValue(Expense expense) {
                return Integer.toString(expense.getPrice());
            }
        };

        expenseTable.addColumn(idColumn, "ID");
        expenseTable.addColumn(typeIdColumn, "Type ID");
        expenseTable.addColumn(nameColumn, "Name");
        expenseTable.addColumn(dateColumn, "Date");
        expenseTable.addColumn(priceColumn, "Price");

        ListDataProvider<Expense> dataProvider = new ListDataProvider<>();

        dataProvider.addDataDisplay(expenseTable);

        List<Expense> list = dataProvider.getList();
        for (Expense expense : expenses) {
            list.add(expense);
        }

        RootPanel.get().add(expenseTable);
*/

        /*final Button sendButton = new Button("Send");
        final TextBox nameField = new TextBox();
        nameField.setText("GWT User");
        final Label errorLabel = new Label();


        RootPanel.get().add(nameField);
        RootPanel.get().add(sendButton);
        RootPanel.get().add(errorLabel);*/
    }
}
