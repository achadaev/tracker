package com.example.tracker.client.services;

import com.example.tracker.shared.model.User;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

public interface UserWebService extends RestService {
    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    void getUser(MethodCallback<User> callback);

    @GET
    @Path("/profileId={id}")
    @Produces(MediaType.APPLICATION_JSON)
    void getUserById(@PathParam("id") int id, MethodCallback<User> callback);

    @GET
    @Path("/all-profiles")
    @Produces(MediaType.APPLICATION_JSON)
    void getAllUsers(MethodCallback<List<User>> callback);

    @POST
    @Path("/add-profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void addUser(User user, MethodCallback<User> callback);

    @PUT
    @Path("/update-profile")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    void updateUser(User user, MethodCallback<User> callback);

    @DELETE
    @Path("/delete-profiles")
    @Consumes(MediaType.APPLICATION_JSON)
    void deleteUsers(List<Integer> ids, MethodCallback<List<User>> callback);
}
