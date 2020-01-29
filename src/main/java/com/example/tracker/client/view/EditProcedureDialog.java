package com.example.tracker.client.view;

import com.example.tracker.client.presenter.EditExpensePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;

public class EditProcedureDialog extends DialogBox implements EditExpensePresenter.Display {
    interface EditExpenseDialogUiBinder extends UiBinder<HTMLPanel, EditProcedureDialog> {
    }

    private static EditExpenseDialogUiBinder ourUiBinder = GWT.create(EditExpenseDialogUiBinder.class);

    @UiField
    DialogBox dialogBox;
    @UiField
    ListBox typeId;
    @UiField
    TextBox name;
    @UiField
    DatePicker date;
    @UiField
    TextBox price;
    @UiField
    FlexTable table;
    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    public EditProcedureDialog() {
        setWidget(ourUiBinder.createAndBindUi(this));
        dialogBox.setAutoHideEnabled(true);
        dialogBox.setGlassEnabled(true);
        initTable();
    }

    private void initTable() {
        name.getElement().setPropertyString("placeholder", "Name");
        date.getElement().setPropertyString("placeholder", "Date");
        price.getElement().setPropertyString("placeholder", "Price");

        table.setWidget(0, 1, typeId);
        table.setWidget(1, 1, name);
        table.setWidget(2, 1, date);
        table.setWidget(3, 1, price);
    }

    @Override
    public ListBox getTypeId() {
        return typeId;
    }

    @Override
    public HasValue<String> getName() {
        return name;
    }

    @Override
    public DatePicker getDate() {
        return date;
    }

    @Override
    public HasValue<String> getPrice() {
        return price;
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    @Override
    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void showDialog() {
        dialogBox.center();
    }

    @Override
    public void hideDialog() {
        dialogBox.hide();
        Window.Location.replace(GWT.getHostPageBaseURL() + "#expense-list");
    }
}