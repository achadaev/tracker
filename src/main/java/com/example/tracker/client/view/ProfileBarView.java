package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.ProfileBarPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class ProfileBarView extends Composite implements ProfileBarPresenter.Display {
    interface ProfileBarViewUiBinder extends UiBinder<HTMLPanel, ProfileBarView> {
    }

    @UiField
    HorizontalPanel horizontalPanel;
    @UiField
    Label usernameLabel;
    @UiField
    Button manageButton;
    @UiField
    Button logoutButton;

    private static ProfileBarViewUiBinder ourUiBinder = GWT.create(ProfileBarViewUiBinder.class);

    public ProfileBarView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        if (!ExpensesGWTController.isAdmin) {
            manageButton.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
        }
    }

    @Override
    public Label getUsernameLabel() {
        return usernameLabel;
    }

    @Override
    public Button getManageButton() {
        return manageButton;
    }

    @Override
    public Button getLogoutButton() {
        return logoutButton;
    }
}