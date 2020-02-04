package com.example.tracker.client.view;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.presenter.MenuBarPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class MenuBarView extends Composite implements MenuBarPresenter.Display {
    interface ProfileBarViewUiBinder extends UiBinder<HTMLPanel, MenuBarView> {
    }

    @UiField
    HorizontalPanel horizontalPanel;
    @UiField
    Label usernameLabel;
    @UiField
    Button manageProfilesButton;
    @UiField
    Button manageTypesButton;
    @UiField
    Button logoutButton;

    private static ProfileBarViewUiBinder ourUiBinder = GWT.create(ProfileBarViewUiBinder.class);

    public MenuBarView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        if (!ExpensesGWTController.isAdmin()) {
            manageProfilesButton.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
            manageTypesButton.getElement().getStyle().setVisibility(Style.Visibility.HIDDEN);
        }
    }

    @Override
    public Label getUsernameLabel() {
        return usernameLabel;
    }

    @Override
    public Button getManageProfilesButton() {
        return manageProfilesButton;
    }

    @Override
    public Button getManageTypesButton() {
        return manageTypesButton;
    }

    @Override
    public Button getLogoutButton() {
        return logoutButton;
    }
}