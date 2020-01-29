package com.example.tracker.client.presenter;

import com.example.tracker.client.event.type.AddTypeEvent;
import com.example.tracker.client.event.type.EditTypeEvent;
import com.example.tracker.client.message.AlertWidget;
import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.shared.model.ProcedureType;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class ManageTypesPresenter implements Presenter {

    private List<ProcedureType> typeList;

    public interface Display {
        HasClickHandlers getAddButton();
        HasClickHandlers getEditButton();
        HasClickHandlers getDeleteButton();
        List<Integer> getSelectedIds();
        void setData(List<ProcedureType> typeList);
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
        display.getAddButton().addClickHandler(clickEvent -> eventBus.fireEvent(new AddTypeEvent()));

        display.getEditButton().addClickHandler(clickEvent -> {
            List<Integer> selectedIds = display.getSelectedIds();

            if (selectedIds.size() == 1) {
                eventBus.fireEvent(new EditTypeEvent(selectedIds.get(0)));
            } else {
                AlertWidget.alert("Error", "Select one row").center();
            }
        });

        display.getDeleteButton().addClickHandler(clickEvent -> deleteSelectedIds());
    }

    private void deleteSelectedIds() {
        List<Integer> selectedIds = display.getSelectedIds();

        typeWebService.deleteTypes(selectedIds, new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error deleting types").center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
                typeList = response;
                display.setData(typeList);
            }
        });
    }

    private void initTable() {
        typeWebService.getTypes(new MethodCallback<List<ProcedureType>>() {
            @Override
            public void onFailure(Method method, Throwable throwable) {
                AlertWidget.alert("Error", "Error getting all types").center();
            }

            @Override
            public void onSuccess(Method method, List<ProcedureType> response) {
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
