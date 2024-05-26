package edu.arep.controller;

import edu.arep.model.Transaction;
import edu.arep.service.TransactionService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/secured/transactions")
@RequestScoped
public class TransactionController {

    @Inject
    TransactionService transactionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTransaction(Transaction transaction) {
        transactionService.addTransaction(transaction);
        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transaction getTransactionById(@PathParam("id") String id) {
        return transactionService.getTransactionById(id);
    }
}
