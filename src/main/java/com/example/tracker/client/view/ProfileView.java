package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.ProfilePresenter;
import com.example.tracker.shared.model.User;
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
    Button refreshButton;
    @UiField
    FlowPanel changePassPanel;
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

    @Override
    public void updateData(User user) {
        setUsername(user.getLogin());
    }

    @Override
    public HasClickHandlers getRefreshButton() {
        return refreshButton;
    }

    private void setUsername(String username) {
        table.setText(0, 0, "Username: ");
        if (!"".equals(username) && username != null) {
            table.setText(0, 1, username);
        } else {
            table.setText(0, 1, "undefined");
        }
    }

    private static ProfileViewUiBinder ourUiBinder = GWT.create(ProfileViewUiBinder.class);

    public ProfileView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        changePassPanel.addStyleName("changePassPanel");
    }
}