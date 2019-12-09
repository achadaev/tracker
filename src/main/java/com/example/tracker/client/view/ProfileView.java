package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.ProfilePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ProfileView extends Composite implements ProfilePresenter.Display {
    interface ProfileViewUiBinder extends UiBinder<HTMLPanel, ProfileView> {
    }

    @UiField
    FlexTable table;
    @UiField
    PasswordTextBox passwordTextBox;
    @UiField
    Button changePassButton;

    @Override
    public PasswordTextBox getPasswordBox() {
        return passwordTextBox;
    }

    @Override
    public HasClickHandlers getChangePassButton() {
        return changePassButton;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    private static ProfileViewUiBinder ourUiBinder = GWT.create(ProfileViewUiBinder.class);

    public ProfileView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        table.setWidget(0, 0, new Label("Username: "));
        table.setWidget(0, 1, new Label(ExpensesGWTController.getUser().getLogin()));
    }
}