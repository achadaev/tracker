package com.example.tracker.client.view;

import com.example.tracker.client.presenter.MainPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MainView extends Composite implements MainPresenter.Display {
    interface MainViewUiBinder extends UiBinder<HTMLPanel, MainView> {
    }

    @UiField
    HorizontalPanel menuPanel;
    @UiField
    Button homeButton;
    @UiField
    Button expensesButton;
    @UiField
    Button profileButton;
    @UiField
    Button calendarButton;
    @UiField
    HTMLPanel profileBarPanel;

    @UiField
    HorizontalPanel contentPanel;

    private static MainViewUiBinder ourUiBinder = GWT.create(MainViewUiBinder.class);

    public MainView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public HorizontalPanel getMenuPanel() {
        return menuPanel;
    }

    @Override
    public HasClickHandlers getHomeButton() {
        return homeButton;
    }

    @Override
    public HasClickHandlers getExpensesButton() {
        return expensesButton;
    }

    @Override
    public HasClickHandlers getProfileButton() {
        return profileButton;
    }

    @Override
    public HasClickHandlers getCalendarButton() {
        return calendarButton;
    }

    @Override
    public HTMLPanel getProfileBarPanel() {
        return profileBarPanel;
    }

    @Override
    public HorizontalPanel getContentPanel() {
        return contentPanel;
    }
}