package com.example.tracker.client.view;

import com.example.tracker.client.presenter.ProfileBarPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class ProfileBarView extends Composite implements ProfileBarPresenter.Display {
    interface ProfileBarViewUiBinder extends UiBinder<HTMLPanel, ProfileBarView> {
    }

    @UiField
    HorizontalPanel horizontalPanel;

    @UiField
    Button logoutButton;

    private static ProfileBarViewUiBinder ourUiBinder = GWT.create(ProfileBarViewUiBinder.class);

    public ProfileBarView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Button getLogoutButton() {
        return logoutButton;
    }
}