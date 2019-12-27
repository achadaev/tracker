package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.ShowExpensesEvent;
import com.example.tracker.client.event.ShowHomeEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.view.ProfileBarView;
import com.example.tracker.shared.model.Expense;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

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

    private ProfileBarPresenter profileBarPresenter;

    public MainPresenter(HandlerManager eventBus, Display display) {
        this.eventBus = eventBus;
        this.display = display;

        profileBarPresenter = new ProfileBarPresenter(new ProfileBarView());
        profileBarPresenter.go(display.getProfileBarPanel());
        bind();
    }

    public void bind() {

        display.getHomeButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new ShowHomeEvent());
            }
        });

        display.getExpensesButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new ShowExpensesEvent());
            }
        });

        display.getProfileButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                eventBus.fireEvent(new ShowProfileEvent());
            }
        });

        display.getCalendarButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                //TODO CalendarEvent
            }
        });
    }

    @Override
    public void go(HasWidgets container) {
    }

    public HorizontalPanel getPanel() {
        return display.getContentPanel();
    }
}
