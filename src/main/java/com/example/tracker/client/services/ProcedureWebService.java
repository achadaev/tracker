package com.example.tracker.client.services;

import com.example.tracker.shared.model.*;
import org.eclipse.jetty.util.Scanner;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ProcedureWebService extends RestService {
    @GET
    @Path("/all-expenses")
    @Produces(MediaType.APPLICATION_JSON)
    void getAllExpenses(MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/all-incomes")
    @Produces(MediaType.APPLICATION_JSON)
    void getAllIncomes(MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/id={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getProcedureById(@PathParam("id") int id, MethodCallback<Procedure> callback);

    @GET
    @Path("/user-expenses")
    @Produces(MediaType.APPLICATION_JSON)
    void getUsersExpenses(MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/user-incomes")
    @Produces(MediaType.APPLICATION_JSON)
    void getUsersIncomes(MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/review/own={isOwn}")
    @Produces(MediaType.APPLICATION_JSON)
    void getReview(@PathParam("isOwn") boolean isOwn, MethodCallback<ReviewInfo> callback);

    @GET
    @Path("/expense-type-review/own={isOwn}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesReviewByTypes(@PathParam("isOwn") boolean isOwn, MethodCallback<Map<String, Double>> callback);

    @GET
    @Path("/income-type-review/own={isOwn}")
    @Produces(MediaType.APPLICATION_JSON)
    void getIncomesReviewByTypes(@PathParam("isOwn") boolean isOwn, MethodCallback<Map<String, Double>> callback);

    @GET
    @Path("/typeId={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getProceduresByTypeId(@PathParam("id") int id, MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/typeId={id}/user={userId}")
    @Produces(MediaType.APPLICATION_JSON)
    void getProceduresByTypeId(@PathParam("id") int id, @PathParam("userId") int userId,
                               MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/expense-typeId={typeId}/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    void getProceduresByDate(@PathParam("typeId") int typeId, @PathParam("startDate") Date startDate,
                             @PathParam("endDate") Date endDate, MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/expense-typeId={typeId}/{startDate}/{endDate}/user={userId}")
    @Produces(MediaType.APPLICATION_JSON)
    void getProceduresByDate(@PathParam("typeId") int typeId, @PathParam("startDate") Date startDate,
                             @PathParam("endDate") Date endDate, @PathParam("userId") int userId,
                             MethodCallback<List<Procedure>> callback);

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void addProcedure(Procedure procedure, MethodCallback<Procedure> callback);

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateProcedure(Procedure procedure, MethodCallback<Procedure> callback);

    @PUT
    @Path("/archive")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void archiveProcedures(List<Integer> ids, MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/dates-between")
    @Produces(MediaType.APPLICATION_JSON)
    void getDatesBetween(MethodCallback<List<SimpleDate>> callback);

    @GET
    @Path("/between/own={isOwn}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesBetween(@PathParam("isOwn") boolean isOwn, MethodCallback<List<MonthlyExpense>> callback);

    @GET
    @Path("/sort/typeId={typeId}/{startDate}/{endDate}/{startIndex}/{quantity}/{column}/{isAscending}")
    @Produces(MediaType.APPLICATION_JSON)
    void getSortedAndFilteredProcedures(@PathParam("typeId") int typeId,
                                        @PathParam("startDate") Date startDate,
                                        @PathParam("endDate") Date endDate,
                                        @PathParam("startIndex") int startIndex,
                                        @PathParam("quantity") int quantity,
                                        @PathParam("column") String column,
                                        @PathParam("isAscending") boolean isAscending,
                                        MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/sort/typeId={typeId}/{startDate}/{endDate}/{startIndex}/{quantity}/{column}/{isAscending}/user={userId}")
    @Produces(MediaType.APPLICATION_JSON)
    void getSortedAndFilteredProcedures(@PathParam("typeId") int typeId,
                                        @PathParam("startDate") Date startDate,
                                        @PathParam("endDate") Date endDate,
                                        @PathParam("startIndex") int startIndex,
                                        @PathParam("quantity") int quantity,
                                        @PathParam("column") String column,
                                        @PathParam("isAscending") boolean isAscending,
                                        @PathParam("userId") int userId,
                                        MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/get-selection/{kind}")
    @Produces(MediaType.APPLICATION_JSON)
    void getSelectionValue(@PathParam("kind") int kind, MethodCallback<SelectionValue> callback);

    @GET
    @Path("/currency")
    @Produces(MediaType.APPLICATION_JSON)
    void getCurrency(MethodCallback<Currency> callback);

}
