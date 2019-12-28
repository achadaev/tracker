package com.example.tracker.client.services;

import com.example.tracker.shared.model.Expense;
import com.example.tracker.shared.model.ExpenseType;
import com.example.tracker.shared.model.ReviewInfo;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateExpense(Expense expense, MethodCallback<Expense> callback);

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteExpenses(List<Integer> ids, MethodCallback<List<Expense>> callback);
}
