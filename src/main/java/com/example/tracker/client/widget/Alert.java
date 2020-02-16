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
import org.gwtbootstrap3.client.ui.Lead;
import org.gwtbootstrap3.client.ui.Modal;

public class Alert extends Composite {
    interface AlertUiBinder extends UiBinder<HTMLPanel, Alert> {
    }

    private static AlertUiBinder ourUiBinder = GWT.create(AlertUiBinder.class);

    @UiField
    Modal modal;
    @UiField
    Heading content;
    @UiField
    Button closeButton;

    public Alert() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    public static void alert(String header, String content) {
        Alert window = new Alert();
        window.modal.setTitle(header);
        window.content.setText(content);
        window.modal.show();
    }

    @UiHandler("closeButton")
    public void onCloseClick(ClickEvent clickEvent) {
        modal.hide();
    }
}