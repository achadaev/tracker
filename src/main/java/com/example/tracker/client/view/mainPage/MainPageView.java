package com.example.tracker.client.view.mainPage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;

public class MainPageView extends Composite {
    interface MainPageViewUiBinder extends UiBinder<HTMLPanel, MainPageView> {
    }

    @UiField
    Button button;
    @UiField
    SimplePanel mainPanel;

    private static MainPageViewUiBinder ourUiBinder = GWT.create(MainPageViewUiBinder.class);

    public MainPageView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }
}