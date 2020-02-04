package com.example.tracker.client.presenter;

import com.example.tracker.client.event.expense.ShowExpensesEvent;
import com.example.tracker.client.event.ShowHomeEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.event.incomes.ShowIncomesEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.view.MenuBarView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;

public class MainPresenter implements Presenter {

    public interface Display {
        HorizontalPanel getMainPanel();
        HasClickHandlers getHomeButton();
        HasClickHandlers getExpensesButton();
        HasClickHandlers getIncomesButton();
        HasClickHandlers getProfileButton();
        HTMLPanel getProfileBarPanel();
        HorizontalPanel getContentPanel();
        Widget asWidget();
    }

    UserWebService userWebService;
    private HandlerManager eventBus;
    private Display display;

    private MenuBarPresenter menuBarPresenter;

    public MainPresenter(UserWebService userWebService, HandlerManager eventBus, Display display) {
        this.userWebService = userWebService;
        this.eventBus = eventBus;
        this.display = display;

        menuBarPresenter = new MenuBarPresenter(userWebService, eventBus, new MenuBarView());
        menuBarPresenter.go(display.getProfileBarPanel());
        bind();
    }

    public void bind() {

        display.getHomeButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowHomeEvent()));

        display.getExpensesButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowExpensesEvent()));

        display.getIncomesButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowIncomesEvent()));

        display.getProfileButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowProfileEvent()));

    }

    @Override
    public void go(HasWidgets container) {
    }

    public HorizontalPanel getContentPanel() {
        return display.getContentPanel();
    }

    public HorizontalPanel getMainPanel() {
        return display.getMainPanel();
    }
}
