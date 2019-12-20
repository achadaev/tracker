package com.example.tracker.client.view;

import com.example.tracker.client.presenter.EditExpensePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DatePicker;

import java.sql.Date;

public class EditExpenseView extends Composite implements EditExpensePresenter.Display {
    interface EditExpenseViewUiBinder extends UiBinder<HTMLPanel, EditExpenseView> {
    }
    @UiField
    ListBox typeId;
    @UiField
    TextBox name;
    @UiField
    DatePicker date;
//    DatePicker date;
    @UiField
    TextBox price;
    @UiField
    FlexTable table;
    @UiField
    Button saveButton;

    private static EditExpenseViewUiBinder ourUiBinder = GWT.create(EditExpenseViewUiBinder.class);

    public EditExpenseView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        initTable();
    }

    private void initTable() {
//        typeId.getElement().setPropertyString("placeholder", "Type ID");
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
//    public HasValue<Date> getDate() {
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
    public Widget asWidget() {
        return this;
    }
}