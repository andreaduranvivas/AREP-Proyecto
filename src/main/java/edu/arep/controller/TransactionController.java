package edu.arep.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import org.bson.Document;
import edu.arep.model.Transaction;
import edu.arep.service.TransactionService;
import edu.arep.service.FraudDetectionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final TransactionService transactionService = new TransactionService();
    private final FraudDetectionService fraudDetectionService = new FraudDetectionService();
    private final Gson gson = new Gson();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String httpMethod = (String) input.get("httpMethod");
        String path = (String) input.get("path");
        Map<String, Object> response = new HashMap<>();

        try {
            if ("POST".equalsIgnoreCase(httpMethod) && "/secured/transactions".equals(path)) {
                return handleAddTransaction(input);
            } else if ("GET".equalsIgnoreCase(httpMethod) && "/secured/transactions".equals(path)) {
                return handleGetTransactions();
            } else if ("GET".equalsIgnoreCase(httpMethod) && path.startsWith("/secured/transactions/")) {
                String id = path.substring("/secured/transactions/".length());
                return handleGetTransactionById(id);
            } else {
                response.put("statusCode", 404);
                response.put("body", "Not Found");
                return response;
            }
        } catch (Exception e) {
            response.put("statusCode", 500);
            response.put("body", "Internal Server Error: " + e.getMessage());
            return response;
        }
    }

    private Map<String, Object> handleAddTransaction(Map<String, Object> input) {
        Map<String, Object> response = new HashMap<>();
        String body = (String) input.get("body");
        Transaction transaction = gson.fromJson(body, Transaction.class);
        transactionService.addTransaction(transaction);
        boolean isFraudulent = fraudDetectionService.isTransactionFraudulent(transaction);
        transaction.setFraudulent(isFraudulent);
        response.put("statusCode", 201);
        response.put("body", gson.toJson(transaction));
        return response;
    }

    private Map<String, Object> handleGetTransactions() {
        Map<String, Object> response = new HashMap<>();
        List<Transaction> transactions = transactionService.getTransactions();
        response.put("statusCode", 200);
        response.put("body", gson.toJson(transactions));
        return response;
    }

    private Map<String, Object> handleGetTransactionById(String id) {
        Map<String, Object> response = new HashMap<>();
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction != null) {
            response.put("statusCode", 200);
            response.put("body", gson.toJson(transaction));
        } else {
            response.put("statusCode", 404);
            response.put("body", "Transaction not found");
        }
        return response;
    }
}
