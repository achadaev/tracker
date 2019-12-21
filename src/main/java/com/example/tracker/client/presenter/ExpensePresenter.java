package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.AddExpenseEvent;
import com.example.tracker.client.event.EditExpenseEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.services.ExpenseWebService;
import com.example.tracker.client.view.ProfileBarView;
import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
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
        ListBox getTypesListBox();
        HasClickHandlers getFilerButton();
        List<Integer> getSelectedIds();
        void setData(List<Expense> data, List<ExpenseType> types);
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

    private void initTypesListBox(ListBox listBox) {
        expenseWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(Method method, List<ExpenseType> expenseTypes) {
                listBox.addItem("All", "0");
                for (ExpenseType type : expenseTypes) {
                    listBox.addItem(type.getName(), Integer.toString(type.getId()));
                }
            }
        });
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

                        display.setData(expenseList, ExpensesGWTController.getTypes());
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

        display.getFilerButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                filterExpenses(Integer.parseInt(display.getTypesListBox().getSelectedValue()));
            }
        });

        initTypesListBox(this.display.getTypesListBox());
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
                display.setData(expenseList, ExpensesGWTController.getTypes());
            }
        });
    }

    private void filterExpenses(int id) {
        expenseWebService.getExpensesByTypeId(id, new MethodCallback<List<Expense>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error filtering expenses");
            }

            @Override
            public void onSuccess(Method method, List<Expense> response) {
                expenseList = response;
                display.setData(expenseList, ExpensesGWTController.getTypes());
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
