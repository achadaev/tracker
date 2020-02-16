package com.example.tracker.client.view;

import com.example.tracker.client.constant.DialogConstants;
import com.example.tracker.client.presenter.EditExpensePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.extras.datepicker.client.ui.DatePicker;
import org.gwtbootstrap3.extras.select.client.ui.Select;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.Button;

import static com.example.tracker.client.constant.PathConstants.EXPENSE_LIST_PATH;
import static com.example.tracker.client.constant.PathConstants.INCOME_LIST_PATH;

public class EditProcedureDialog extends Composite implements EditExpensePresenter.Display {
    interface EditExpenseDialogUiBinder extends UiBinder<Widget, EditProcedureDialog> {
    }

    private static EditExpenseDialogUiBinder ourUiBinder = GWT.create(EditExpenseDialogUiBinder.class);

    private int procedureKind;

    @UiField
    Modal editModal;
    @UiField
    Select typeId;
    @UiField
    TextBox name;
    @UiField
    DatePicker date;
    @UiField
    TextBox price;
    @UiField
    Button saveButton;
    @UiField
    Button cancelButton;

    public EditProcedureDialog(int procedureKind) {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.procedureKind = procedureKind;
        editModal.setDataBackdrop(ModalBackdrop.STATIC);
        editModal.setClosable(false);

        if (procedureKind < 0) {
            editModal.setTitle(DialogConstants.EDIT_EXPENSE_HEADER);
        } else {
            editModal.setTitle(DialogConstants.EDIT_INCOME_HEADER);
        }
    }

    @Override
    public void show() {
        editModal.show();
    }

    @Override
    public void hide() {
        editModal.hide();
        if (procedureKind < 0) {
            Window.Location.replace(GWT.getHostPageBaseURL() + "#" + EXPENSE_LIST_PATH);
        } else {
            Window.Location.replace(GWT.getHostPageBaseURL() + "#" + INCOME_LIST_PATH);
        }
    }

    @Override
    public Select getTypeId() {
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
}