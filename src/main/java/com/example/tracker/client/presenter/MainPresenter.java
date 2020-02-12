package com.example.tracker.client.presenter;

import com.example.tracker.client.ExpensesGWTController;
import com.example.tracker.client.event.expense.ShowExpensesEvent;
import com.example.tracker.client.event.ShowHomeEvent;
import com.example.tracker.client.event.ShowProfileEvent;
import com.example.tracker.client.event.incomes.ShowIncomesEvent;
import com.example.tracker.client.services.UserWebService;
import com.example.tracker.client.view.MenuBarView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.*;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.Pull;

public class MainPresenter implements Presenter {

    public interface Display {
        NavbarNav getNavigationBar();
        HasClickHandlers getHomeButton();
        HasClickHandlers getExpensesButton();
        HasClickHandlers getIncomesButton();
        HasClickHandlers getProfileButton();
        HorizontalPanel getMenuPanel();
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
        if (ExpensesGWTController.isAdmin()) {
            display.getNavigationBar().add(menuBarPresenter.getManageTypesButton());
            display.getNavigationBar().add(menuBarPresenter.getManageProfilesButton());
        }
        display.getNavigationBar().add(menuBarPresenter.getLogoutButton());
        //menuBarPresenter.go(display.getMenuPanel());
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

}
