package com.example.tracker.client.view;

import com.example.tracker.client.presenter.HomePresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class HomeView extends Composite implements HomePresenter.Display {
    interface HomeViewUiBinder extends UiBinder<HTMLPanel, HomeView> {
    }

    @UiField
    Label greetingLabel;
    @UiField
    HTMLPanel reviewPanel;
    @UiField
    Label amountLabel;
    @UiField
    Label monthLabel;
    @UiField
    Label weekLabel;
    @UiField
    Label moreLabel;

    private static HomeViewUiBinder ourUiBinder = GWT.create(HomeViewUiBinder.class);

    public HomeView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public Label getGreetingLabel() {
        return greetingLabel;
    }

    @Override
    public Panel getReviewPanel() {
        return reviewPanel;
    }

    @Override
    public Label getAmountLabel() {
        return amountLabel;
    }

    @Override
    public Label getMonthLabel() {
        return monthLabel;
    }

    @Override
    public Label getWeekLabel() {
        return weekLabel;
    }

    @Override
    public Label getMoreLabel() {
        return moreLabel;
    }

    @Override
    public Widget asWidget() {
        return this;
    }
}