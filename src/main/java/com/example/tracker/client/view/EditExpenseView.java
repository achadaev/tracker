package com.example.tracker.client.view;

import com.example.tracker.client.presenter.EditExpensePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class EditExpenseView extends Composite implements EditExpensePresenter.Display {
    interface EditExpenseViewUiBinder extends UiBinder<HTMLPanel, EditExpenseView> {
    }
    @UiField
    TextBox id;
    @UiField
    TextBox typeId;
    @UiField
    TextBox name;
    @UiField
    TextBox date;
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
        table.setWidget(0, 0, new Label("ID"));
        table.setWidget(0, 1, id);
        table.setWidget(1, 0, new Label("Type ID"));
        table.setWidget(1, 1, typeId);
        table.setWidget(2, 0, new Label("Name"));
        table.setWidget(2, 1, name);
        table.setWidget(3, 0, new Label("Date"));
        table.setWidget(3, 1, date);
        table.setWidget(4, 0, new Label("Price"));
        table.setWidget(4, 1, price);
        typeId.setFocus(true);
    }

    //TODO HasValue<integer>

    @Override
    public HasValue getId() {
        return id;
    }

    @Override
    public HasValue getTypeId() {
        return typeId;
    }

    @Override
    public HasValue<String> getName() {
        return name;
    }

    @Override
    public HasValue<String> getDate() {
        return date;
    }

    @Override
    public HasValue getPrice() {
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