package com.example.tracker.client.services;

import com.example.tracker.shared.model.ExpenseType;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface TypeWebService extends RestService {
    @GET
    @Path("/types")
    @Produces(MediaType.APPLICATION_JSON)
    void getTypes(MethodCallback<List<ExpenseType>> callback);

    @GET
    @Path("/typesId={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getTypeById(@PathParam("id") int id, MethodCallback<ExpenseType> callback);

    @POST
    @Path("/add-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void addType(ExpenseType type, MethodCallback<ExpenseType> callback);

    @PUT
    @Path("/update-type")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateType(ExpenseType type, MethodCallback<ExpenseType> callback);

    @DELETE
    @Path("/delete-types")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteTypes(List<Integer> ids, MethodCallback<List<ExpenseType>> callback);

}
