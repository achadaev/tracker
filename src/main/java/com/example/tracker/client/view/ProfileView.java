package com.example.tracker.client.view;

import com.example.tracker.client.presenter.ProfilePresenter;
import com.example.tracker.shared.model.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ProfileView extends Composite implements ProfilePresenter.Display {
    interface ProfileViewUiBinder extends UiBinder<HTMLPanel, ProfileView> {
    }

    @UiField
    FlexTable table;
    @UiField
    Button editProfile;

    private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd-MM-yyyy hh:mm:ss");

    private static ProfileViewUiBinder ourUiBinder = GWT.create(ProfileViewUiBinder.class);

    public ProfileView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public void setData(User user) {
        table.setText(0, 0, "Login: ");
        table.setText(0, 1, user.getLogin());
        table.setText(1, 0, "Name: ");
        table.setText(1, 1, user.getName());
        table.setText(2, 0, "Surname: ");
        table.setText(2, 1, user.getSurname());
        table.setText(3, 0, "Email: ");
        table.setText(3, 1, user.getEmail());
        table.setText(4, 0, "Role: ");
        table.setText(4, 1, user.getRole());
        table.setText(5, 0, "Registration Date: ");
        table.setText(5, 1, dateTimeFormat.format(user.getRegDate()));
    }

    @Override
    public HasClickHandlers getEditProfileButton() {
        return editProfile;
    }
}