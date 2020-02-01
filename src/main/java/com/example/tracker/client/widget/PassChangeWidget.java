package com.example.tracker.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class PassChangeWidget {
    private Changer changer;

    public interface Changer {
        void onChange(TextBox passBox, TextBox repeatPassBox);
    }

    public PassChangeWidget(Changer changer) {
        this.changer = changer;
    }

    public DialogBox change(String header) {
        DialogBox dialogBox = new DialogBox();
        VerticalPanel panel = new VerticalPanel();
        HorizontalPanel buttonsPanel = new HorizontalPanel();

        PasswordTextBox passBox = new PasswordTextBox();
        PasswordTextBox repeatPassBox = new PasswordTextBox();

        Button confirmButton = new Button("Confirm", (ClickHandler) event -> {
            changer.onChange(passBox, repeatPassBox);
            dialogBox.hide();
        });
        Button cancelButton = new Button("Cancel", (ClickHandler) event -> dialogBox.hide());

        dialogBox.setGlassEnabled(true);
        dialogBox.setText(header);

        panel.setSpacing(10);
        panel.add(new Label("Enter new password"));
        panel.add(passBox);
        panel.add(new Label("Repeat new password"));
        panel.add(repeatPassBox);

        buttonsPanel.setSpacing(7);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        panel.add(buttonsPanel);
        dialogBox.add(panel);

        return dialogBox;
    }
}
