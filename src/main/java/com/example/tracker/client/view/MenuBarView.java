package com.example.tracker.client.view;

import com.example.tracker.client.presenter.MenuBarPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

public class MenuBarView extends Composite implements MenuBarPresenter.Display {
    interface ProfileBarViewUiBinder extends UiBinder<HTMLPanel, MenuBarView> {
    }

    private AnchorListItem manageProfilesButton;
    private AnchorListItem manageTypesButton;
    private AnchorListItem logoutButton;

    private static ProfileBarViewUiBinder ourUiBinder = GWT.create(ProfileBarViewUiBinder.class);

    public MenuBarView() {
        initWidget(ourUiBinder.createAndBindUi(this));
        manageTypesButton = new AnchorListItem();
        manageProfilesButton = new AnchorListItem();
        logoutButton = new AnchorListItem();

        manageTypesButton.setText("Manage Types");
        manageProfilesButton.setText("Manage Profiles");
        logoutButton.setText("Logout");

        manageTypesButton.setIcon(IconType.BARS);
        manageProfilesButton.setIcon(IconType.USERS);
        logoutButton.setIcon(IconType.CLOSE);

        logoutButton.setPull(Pull.RIGHT);
    }

    @Override
    public AnchorListItem getManageProfilesButton() {
        return manageProfilesButton;
    }

    @Override
    public AnchorListItem getManageTypesButton() {
        return manageTypesButton;
    }

    @Override
    public AnchorListItem getLogoutButton() {
        return logoutButton;
    }
}