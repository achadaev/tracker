package com.example.tracker.client.presenter;

import com.example.tracker.client.event.type.AddTypeEvent;
import com.example.tracker.client.event.type.EditTypeEvent;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.ExpenseType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ManageTypesPresenter implements Presenter {

    private List<ExpenseType> typeList;

    public interface Display {
        HasClickHandlers getAddButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        List<Integer> getSelectedIds();
        void setData(List<ExpenseType> typeList);
        Widget asWidget();
    }

    private TypeWebService typeWebService;
    private HandlerManager eventBus;
    private Display display;

    public ManageTypesPresenter(TypeWebService typeWebService, HandlerManager eventBus, Display display) {
        this.typeWebService = typeWebService;
        this.eventBus = eventBus;
        this.display = display;
    }

    public void bind() {
        display.getAddButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new AddTypeEvent());
            }
        });

        display.getEditButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                List<Integer> selectedIds = display.getSelectedIds();

                if (selectedIds.size() == 1) {
                    eventBus.fireEvent(new EditTypeEvent(selectedIds.get(0)));
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

        typeWebService.deleteTypes(selectedIds, new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error deleting types");
            }

            @Override
            public void onSuccess(Method method, List<ExpenseType> response) {
                typeList = response;
                display.setData(typeList);
            }
        });
    }

    private void initTable() {
        typeWebService.getTypes(new MethodCallback<List<ExpenseType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                Window.alert("Error getting all users");
            }

            @Override
            public void onSuccess(Method method, List<ExpenseType> response) {
                typeList = response;
                display.setData(typeList);
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
        bind();
        container.clear();
        container.add(display.asWidget());
        initTable();
    }
}
