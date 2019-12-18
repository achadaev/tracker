package com.example.tracker.client.services;

import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
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
    @Path("/id={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpenseById(@PathParam("id") int id, MethodCallback<Expense> callback);

    @GET
    @Path("/user")
    @Produces(MediaType.APPLICATION_JSON)
    void getUsersExpenses(MethodCallback<List<Expense>> callback);

    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    void getTypes(MethodCallback<List<ExpenseType>> callback);
    
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void addExpense(Expense expense, MethodCallback<Expense> callback);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void updateExpense(Expense expense, MethodCallback<Expense> callback);

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteExpenses(List<Integer> ids, MethodCallback<List<Expense>> callback);
}
