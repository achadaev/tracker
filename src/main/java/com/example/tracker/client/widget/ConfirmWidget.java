package com.example.tracker.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class ConfirmWidget {
    private Confirmation confirmation;

    public interface Confirmation {
        void onConfirm();
    }

    public ConfirmWidget(Confirmation confirmation) {
        this.confirmation = confirmation;
    }

    public DialogBox confirm(String header, String content) {
        DialogBox dialogBox = new DialogBox();
        VerticalPanel panel = new VerticalPanel();
        HorizontalPanel buttonsPanel = new HorizontalPanel();

        Button confirmButton = new Button("Confirm", (ClickHandler) event -> {
            confirmation.onConfirm();
            dialogBox.hide();
        });
        Button cancelButton = new Button("Cancel", (ClickHandler) event -> dialogBox.hide());

        dialogBox.setGlassEnabled(true);
        dialogBox.setText(header);

        panel.setSpacing(10);
        panel.add(new Label(content));

        buttonsPanel.setSpacing(7);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        panel.add(buttonsPanel);
        dialogBox.add(panel);

        return dialogBox;
    }
}
