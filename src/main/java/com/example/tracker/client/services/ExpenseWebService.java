package com.example.tracker.client.services;

import com.example.tracker.shared.model.Expense;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface ExpenseWebService extends RestService {

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    void getAllExpenses(MethodCallback<List<Expense>> callback);

    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesByUser(@PathParam("login") String login, MethodCallback<List<Expense>> callback);
    
    @POST
    void addExpense(Expense expense, MethodCallback<Expense> callback);
}
