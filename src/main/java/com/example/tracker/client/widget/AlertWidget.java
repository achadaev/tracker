package com.example.tracker.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AlertWidget {
    public static DialogBox alert(String header, String content) {
        DialogBox dialogBox = new DialogBox();
        VerticalPanel panel = new VerticalPanel();
        Button closeButton = new Button("Close", (ClickHandler) event -> dialogBox.hide());

        dialogBox.setGlassEnabled(true);
        dialogBox.setText(header);

        panel.setSpacing(10);
        panel.add(new Label(content));
        panel.add(closeButton);

        dialogBox.add(panel);

        return dialogBox;
    }
}
