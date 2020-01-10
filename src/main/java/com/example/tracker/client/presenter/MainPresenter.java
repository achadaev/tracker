package com.example.tracker.client.presenter;

import com.example.tracker.client.event.expense.ShowExpensesEvent;
import com.example.tracker.client.event.ShowHomeEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.view.MenuBarView;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;

public class MainPresenter implements Presenter {

    public interface Display {
        HorizontalPanel getMenuPanel();
        HasClickHandlers getHomeButton();
        HasClickHandlers getExpensesButton();
        HasClickHandlers getProfileButton();
        HasClickHandlers getCalendarButton();
        HTMLPanel getProfileBarPanel();
        HorizontalPanel getContentPanel();
        Widget asWidget();
    }

    private HandlerManager eventBus;
    private Display display;

    private MenuBarPresenter menuBarPresenter;

    public MainPresenter(HandlerManager eventBus, Display display) {
        this.eventBus = eventBus;
        this.display = display;

        menuBarPresenter = new MenuBarPresenter(eventBus, new MenuBarView());
        menuBarPresenter.go(display.getProfileBarPanel());
        bind();
    }

    public void bind() {

        display.getHomeButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowHomeEvent()));

        display.getExpensesButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowExpensesEvent()));

        display.getProfileButton().addClickHandler(clickEvent -> eventBus.fireEvent(new ShowProfileEvent()));

        display.getCalendarButton().addClickHandler(clickEvent -> {
            //TODO CalendarEvent
        });
    }

    @Override
    public void go(HasWidgets container) {
    }

    public HorizontalPanel getPanel() {
        return display.getContentPanel();
    }
}
