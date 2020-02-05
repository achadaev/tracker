package com.example.tracker.client.widget;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import static com.example.tracker.client.constant.WidgetConstants.*;

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

        Button confirmButton = new Button(CONFIRM_BUTTON, (ClickHandler) event -> {
            changer.onChange(passBox, repeatPassBox);
            dialogBox.hide();
        });
        Button cancelButton = new Button(CANCEL_BUTTON, (ClickHandler) event -> dialogBox.hide());

        dialogBox.setGlassEnabled(true);
        dialogBox.setText(header);

        panel.setSpacing(10);
        panel.add(new Label(ENTER_PASSWORD_LABEL));
        panel.add(passBox);
        panel.add(new Label(REPEAT_PASSWORD_LABEL));
        panel.add(repeatPassBox);

        buttonsPanel.setSpacing(7);
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        panel.add(buttonsPanel);
        dialogBox.add(panel);

        return dialogBox;
    }
}
