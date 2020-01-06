package com.example.tracker.client.view;

import com.example.tracker.client.presenter.EditTypePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class EditTypeView extends Composite implements EditTypePresenter.Display {
    interface EditTypeViewUiBinder extends UiBinder<HTMLPanel, EditTypeView> {
    }

    @UiField
    FlexTable table;
    @UiField
    Button saveButton;

    TextBox name;

    private static EditTypeViewUiBinder ourUiBinder = GWT.create(EditTypeViewUiBinder.class);

    public EditTypeView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        name = new TextBox();
        initTable();
    }

    private void initTable() {
        table.setText(0, 0, "Name");
        table.setWidget(0, 1, name);
    }

    @Override
    public HasClickHandlers getSaveButton() {
        return saveButton;
    }

    @Override
    public HasValue<String> getName() {
        return name;
    }
}