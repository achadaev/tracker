package com.example.tracker.client.services;

import com.example.tracker.shared.model.Expense;
import com.google.gwt.editor.client.Editor;
import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.RestService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/expenses")
public interface ExpenseWebService extends RestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    void getAllExpenses(MethodCallback<List<Expense>> callback);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{login}")
    void getExpensesByUser(@PathParam("login") String login, MethodCallback<List<Expense>> callback);
}
