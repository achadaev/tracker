package com.example.tracker.client;

import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.mainPage.MainPageView;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensesGWTApplication implements EntryPoint {
    @Override
    public void onModuleLoad() {


        ExpenseWebService expenseWebService = GWT.create(ExpenseWebService.class);
        Button button = new Button("Get All Expenses");
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                expenseWebService.getAllExpenses(new MethodCallback<List<Expense>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        RootPanel.get().add(new Label("Error"));
                    }

                    @Override
                    public void onSuccess(Method method, List<Expense> response) {
                        CellTable<Expense> table = new CellTable<>();
                        TextColumn<Expense> idColumn = new TextColumn<Expense>() {
                            @Override
                            public String getValue(Expense expense) {
                                return Integer.toString(expense.getId());
                            }
                        };
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
                        table.addColumn(idColumn, "ID");
                        table.addColumn(typeIdColumn, "Type ID");
                        table.addColumn(nameColumn, "Name");
                        table.addColumn(dateColumn, "Date");
                        table.addColumn(priceColumn, "Price");
                        ListDataProvider<Expense> dataProvider = new ListDataProvider<>();
                        dataProvider.addDataDisplay(table);
                        List<Expense> list = dataProvider.getList();

                        for (Expense expense : response) {
                            list.add(expense);
                        }
                        RootPanel.get().add(table);
                    }
                });
            }
        });
        RootPanel.get().add(button);
    }
}
