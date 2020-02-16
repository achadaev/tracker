package com.example.tracker.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Heading;
import org.gwtbootstrap3.client.ui.Modal;

public class Confirm extends Composite {
    interface ConfirmModalUiBinder extends UiBinder<HTMLPanel, Confirm> {
    }

    private static ConfirmModalUiBinder ourUiBinder = GWT.create(ConfirmModalUiBinder.class);

    public interface Confirmation {
        void onConfirm();
    }

    @UiField
    Modal modal;
    @UiField
    Heading content;
    @UiField
    Button confirmButton;
    @UiField
    Button cancelButton;

    private Confirmation confirmation;

    public Confirm() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public static void confirm(Confirmation confirmation, String header, String content) {
        Confirm window = new Confirm();
        window.confirmation = confirmation;
        window.modal.setTitle(header);
        window.content.setText(content);
        window.modal.show();
    }

    @UiHandler("confirmButton")
    public void onConfirmClick(ClickEvent clickEvent) {
        confirmation.onConfirm();
        modal.hide();
    }

    @UiHandler("cancelButton")
    public void onCloseClick(ClickEvent clickEvent) {
        modal.hide();
    }
}