package com.example.tracker.client.services;

import com.example.tracker.shared.model.User;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface UserWebService extends RestService {
    @GET
    @Path("/profile")
    @Produces(MediaType.APPLICATION_JSON)
    void getUser(MethodCallback<User> callback);
}
