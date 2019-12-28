package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.ProfilePresenter;
import com.example.tracker.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.client.ui.*;

public class ProfileView extends Composite implements ProfilePresenter.Display {
    interface ProfileViewUiBinder extends UiBinder<HTMLPanel, ProfileView> {
    }

    @UiField
    FlexTable table;
    @UiField
    FlowPanel changePassPanel;
    @UiField
    PasswordTextBox passwordTextBox;
    @UiField
    Button changePassButton;

    private static ProfileViewUiBinder ourUiBinder = GWT.create(ProfileViewUiBinder.class);

    public ProfileView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        changePassPanel.addStyleName("changePassPanel");
    }

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
    public void setData(User user) {
        table.setText(0, 0, "Username: ");
        table.setText(0, 1, ExpensesGWTController.getUser().getLogin());
        table.setText(1, 0, "Name: ");
        table.setText(1, 1, ExpensesGWTController.getUser().getName());
        table.setText(2, 0, "Surname: ");
        table.setText(2, 1, ExpensesGWTController.getUser().getSurname());
        table.setText(3, 0, "Email: ");
        table.setText(3, 1, ExpensesGWTController.getUser().getEmail());
    }
}