package com.example.tracker.client.services;

import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.MonthlyExpense;
import com.example.tracker.shared.model.ReviewInfo;
import com.example.tracker.shared.model.SimpleDate;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
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
    @Path("/user-expenses")
    @Produces(MediaType.APPLICATION_JSON)
    void getUsersExpenses(MethodCallback<List<Expense>> callback);

    @GET
    @Path("/review")
    @Produces(MediaType.APPLICATION_JSON)
    void getReview(MethodCallback<ReviewInfo> callback);

    @GET
    @Path("/typeId={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesByTypeId(@PathParam("id") int id, MethodCallback<List<Expense>> callback);

    @GET
    @Path("/typeId={typeId}/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesByDate(@PathParam("typeId") int typeId, @PathParam("startDate") Date startDate,
                           @PathParam("endDate") Date endDate, MethodCallback<List<Expense>> callback);

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void addExpense(Expense expense, MethodCallback<Expense> callback);

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateExpense(Expense expense, MethodCallback<Expense> callback);

    @PUT
    @Path("/archive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void archiveExpenses(List<Integer> ids, MethodCallback<List<Expense>> callback);

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteExpenses(List<Integer> ids, MethodCallback<List<Expense>> callback);

    @GET
    @Path("/dates-between")
    @Produces(MediaType.APPLICATION_JSON)
    void getDatesBetween(MethodCallback<List<SimpleDate>> callback);

    @GET
    @Path("/between")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesBetween(MethodCallback<List<MonthlyExpense>> callback);

}
