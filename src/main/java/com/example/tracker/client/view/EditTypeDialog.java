package com.example.tracker.client.view;

import com.example.tracker.client.constant.DialogConstants;
import com.example.tracker.client.presenter.EditTypePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.Radio;
import org.gwtbootstrap3.client.ui.TextBox;

import static com.example.tracker.client.constant.PathConstants.MANAGE_TYPES_PATH;

public class EditTypeDialog extends DialogBox implements EditTypePresenter.Display {
    interface EditTypeDialogUiBinder extends UiBinder<HTMLPanel, EditTypeDialog> {
    }

    private static EditTypeDialogUiBinder ourUiBinder = GWT.create(EditTypeDialogUiBinder.class);

    @UiField
    Modal modal;
    @UiField
    TextBox name;
    @UiField
    Radio expenseRadio;
    @UiField
    Radio incomeRadio;
    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    public EditTypeDialog() {
        setWidget(ourUiBinder.createAndBindUi(this));
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
    public Radio getExpenseRadio() {
        return expenseRadio;
    }

    @Override
    public Radio getIncomeRadio() {
        return incomeRadio;
    }

    @Override
    public void show() {
        modal.show();
    }

    @Override
    public void hide() {
        modal.hide();
        Window.Location.replace(GWT.getHostPageBaseURL() + "#" + MANAGE_TYPES_PATH);
    }
}