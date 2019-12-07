package com.example.tracker.client.presenter;

import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensePresenter implements Presenter {

    private List<Expense> expenseList;

    public interface Display {
        HasClickHandlers getAllExpensesButton();
        HasClickHandlers getAddButton();
        void setData(List<Expense> data);
        Widget asWidget();
    }

    private ExpenseWebService expenseWebService;
    private HandlerManager eventBus;
    private Display display;

    public ExpensePresenter(ExpenseWebService expenseWebService, HandlerManager eventBus, Display view) {
        this.expenseWebService = expenseWebService;
        this.eventBus = eventBus;
        this.display = view;
    }

    public void bind() {
        display.getAllExpensesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                expenseWebService.getAllExpenses(new MethodCallback<List<Expense>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert("Error fetching expenses");
                    }

                    @Override
                    public void onSuccess(Method method, List<Expense> response) {
                        expenseList = response;
                        display.setData(expenseList);
                    }
                });

/*                expenseWebService.getAllExpenses(new MethodCallback<List<Expense>>() {
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
                        RootPanel.get().clear();
                        RootPanel.get().add(button);
                        RootPanel.get().add(table);
                    }*/
            }
        });

        display.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.alert("Adding soon");
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
    }
}
