package com.example.tracker.client.services;

import com.example.tracker.shared.model.ProcedureType;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface TypeWebService extends RestService {
    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    void getTypes(MethodCallback<List<ProcedureType>> callback);

    @GET
    @Path("/expense-types")
    @Produces(MediaType.APPLICATION_JSON)
    void getExpenseTypes(MethodCallback<List<ProcedureType>> callback);

    @GET
    @Path("/income-types")
    @Produces(MediaType.APPLICATION_JSON)
    void getIncomeTypes(MethodCallback<List<ProcedureType>> callback);

    @GET
    @Path("/typesId={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getTypeById(@PathParam("id") int id, MethodCallback<ProcedureType> callback);

    @POST
    @Path("/add-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void addType(ProcedureType type, MethodCallback<ProcedureType> callback);

    @PUT
    @Path("/update-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateType(ProcedureType type, MethodCallback<ProcedureType> callback);

    @DELETE
    @Path("/delete-types")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteTypes(List<Integer> ids, MethodCallback<List<ProcedureType>> callback);

}
