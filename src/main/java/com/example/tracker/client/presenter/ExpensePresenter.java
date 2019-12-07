package com.example.tracker.client.presenter;

import com.example.tracker.client.event.AddExpenseEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.ExpenseView;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.core.client.GWT;
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

import java.util.ArrayList;
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

    private String stackTraceToString(Throwable e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public void bind() {
        display.getAllExpensesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                expenseWebService.getAllExpenses(new MethodCallback<List<Expense>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert(exception.getLocalizedMessage());
                    }

                    @Override
                    public void onSuccess(Method method, List<Expense> response) {
                        expenseList = response;
                        display.setData(expenseList);
                    }
                });
            }
        });

        display.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new AddExpenseEvent());
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
