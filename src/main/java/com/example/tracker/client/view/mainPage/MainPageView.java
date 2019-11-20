package com.example.tracker.client.view.mainPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;

public class MainPageView extends Composite {
    interface MainPageViewUiBinder extends UiBinder<HTMLPanel, MainPageView> {
    }

    private static MainPageViewUiBinder ourUiBinder = GWT.create(MainPageViewUiBinder.class);

    public MainPageView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}