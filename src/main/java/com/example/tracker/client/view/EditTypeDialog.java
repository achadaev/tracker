package com.example.tracker.client.view;

import com.example.tracker.client.presenter.EditTypePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class EditTypeDialog extends DialogBox implements EditTypePresenter.Display {
    interface EditTypeDialogUiBinder extends UiBinder<HTMLPanel, EditTypeDialog> {
    }

    private static EditTypeDialogUiBinder ourUiBinder = GWT.create(EditTypeDialogUiBinder.class);

    @UiField
    DialogBox dialogBox;
    @UiField
    FlexTable table;
    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    TextBox name;
    RadioButton expenseRadio;
    RadioButton incomeRadio;

    public EditTypeDialog() {
        setWidget(ourUiBinder.createAndBindUi(this));
        dialogBox.setGlassEnabled(true);
        dialogBox.setText("Edit Type");
        name = new TextBox();
        expenseRadio = new RadioButton("kind", "Expense");
        incomeRadio = new RadioButton("kind", "Income");
        initTable();
    }

    private void initTable() {
        table.setText(0, 0, "Name");
        table.setWidget(0, 1, name);
        table.setText(1, 0, "Kind");
        table.setWidget(1, 1, expenseRadio);
        table.setWidget(2, 1, incomeRadio);
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
    public HasValue<String> getName() {
        return name;
    }

    @Override
    public RadioButton getExpenseRadio() {
        return expenseRadio;
    }

    @Override
    public RadioButton getIncomeRadio() {
        return incomeRadio;
    }

    @Override
    public void showDialog() {
        dialogBox.center();
    }

    @Override
    public void hideDialog() {
        dialogBox.hide();
        Window.Location.replace(GWT.getHostPageBaseURL() + "#manage-types");
    }
}