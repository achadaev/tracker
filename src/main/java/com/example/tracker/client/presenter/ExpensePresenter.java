package com.example.tracker.client.presenter;

import com.example.tracker.client.event.AddExpenseEvent;
import com.example.tracker.client.event.EditExpenseEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.ProfileBarView;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ExpensePresenter implements Presenter {

    private List<Expense> expenseList;

    public interface Display {
        HasClickHandlers getExpensesButton();
        HasClickHandlers getAddButton();
        HasClickHandlers getProfileButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        HTMLPanel getProfileBarPanel();
        List<Integer> getSelectedIds();
        void setData(List<Expense> data);
        Widget asWidget();
    }

    private ExpenseWebService expenseWebService;
    private HandlerManager eventBus;
    private Display display;

    private ProfileBarPresenter profileBarPresenter;

    public ExpensePresenter(ExpenseWebService expenseWebService, HandlerManager eventBus, Display view) {
        this.expenseWebService = expenseWebService;
        this.eventBus = eventBus;
        this.display = view;

        profileBarPresenter = new ProfileBarPresenter(new ProfileBarView());
        profileBarPresenter.go(display.getProfileBarPanel());
    }

    public void bind() {
        display.getExpensesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                expenseWebService.getUsersExpenses(new MethodCallback<List<Expense>>() {
                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert(exception.getMessage());
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

        display.getProfileButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new ShowProfileEvent());
            }
        });

        display.getEditButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                List<Integer> selectedIds = display.getSelectedIds();

                if (selectedIds.size() == 1) {
                    eventBus.fireEvent(new EditExpenseEvent(selectedIds.get(0)));
                } else {
                    Window.alert("Select one row");
                }
            }
        });



        display.getDeleteButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                deleteSelectedIds();
            }
        });

    }


    private void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        expenseWebService.deleteExpenses(selectedIds, new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable exception) {
                Window.alert("Error deleting expenses");
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
                display.setData(expenseList);
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
