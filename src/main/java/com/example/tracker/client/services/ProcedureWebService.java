package com.example.tracker.client.services;

import com.example.tracker.shared.model.Procedure;
import com.example.tracker.shared.model.MonthlyExpense;
import com.example.tracker.shared.model.ReviewInfo;
import com.example.tracker.shared.model.SimpleDate;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

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
    @Path("/review")
    @Produces(MediaType.APPLICATION_JSON)
    void getReview(MethodCallback<ReviewInfo> callback);

    @GET
    @Path("/typeId={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getProceduresByTypeId(@PathParam("id") int id, MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/expense-typeId={typeId}/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesByDate(@PathParam("typeId") int typeId, @PathParam("startDate") Date startDate,
                           @PathParam("endDate") Date endDate, MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/income-typeId={typeId}/{startDate}/{endDate}")
    @Produces(MediaType.APPLICATION_JSON)
    void getIncomesByDate(@PathParam("typeId") int typeId, @PathParam("startDate") Date startDate,
                           @PathParam("endDate") Date endDate, MethodCallback<List<Procedure>> callback);

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
    void archiveProcedure(List<Integer> ids, MethodCallback<List<Procedure>> callback);

    @DELETE
    @Path("/delete")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteProcedure(List<Integer> ids, MethodCallback<List<Procedure>> callback);

    @GET
    @Path("/dates-between")
    @Produces(MediaType.APPLICATION_JSON)
    void getDatesBetween(MethodCallback<List<SimpleDate>> callback);

    @GET
    @Path("/between")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpensesBetween(MethodCallback<List<MonthlyExpense>> callback);

    @GET
    @Path("/sort/typeId={typeId}/{startDate}/{endDate}/{startIndex}/{quantity}/{isAscending}")
    @Produces(MediaType.APPLICATION_JSON)
    void getSortedAndFilteredProdecures(@PathParam("typeId") int typeId,
                                        @PathParam("startDate") Date startDate,
                                        @PathParam("endDate") Date endDate,
                                        @PathParam("startIndex") int startIndex,
                                        @PathParam("quantity") int quantity,
                                        @PathParam("isAscending") boolean isAscending,
                                        MethodCallback<List<Procedure>> callback);

}
