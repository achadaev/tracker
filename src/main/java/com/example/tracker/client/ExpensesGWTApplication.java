package com.example.tracker.client;

import com.example.tracker.client.services.TypeWebService;
import com.example.tracker.client.services.ProcedureWebService;
import com.example.tracker.client.services.UserWebService;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;

public class ExpensesGWTApplication implements EntryPoint {
    @Override
    public void onModuleLoad() {
        ProcedureWebService procedureWebService = GWT.create(ProcedureWebService.class);
        TypeWebService typeWebService = GWT.create(TypeWebService.class);
        UserWebService userWebService = GWT.create(UserWebService.class);
        HandlerManager eventBus = new HandlerManager(null);
        new ExpensesGWTController(procedureWebService, typeWebService, userWebService, eventBus);
    }
}
