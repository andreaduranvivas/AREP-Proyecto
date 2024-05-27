package edu.arep.controller;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.Gson;
import org.bson.Document;
import edu.arep.model.Transaction;
import edu.arep.service.TransactionService;
import edu.arep.service.FraudDetectionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController implements RequestHandler<Map<String, Object>, List<Map<String, Object>>> {

    private final TransactionService transactionService = new TransactionService();
    private final FraudDetectionService fraudDetectionService = new FraudDetectionService();
    private final Gson gson = new Gson();

    @Override
    public List<Map<String, Object>> handleRequest(Map<String, Object> input, Context context) {
        String httpMethod = (String) input.get("httpMethod");
        String path = (String) input.get("path");

        try {
            if ("POST".equalsIgnoreCase(httpMethod) && "/secured/transactions".equals(path)) {
                return handleAddTransaction(input);
            } else if ("GET".equalsIgnoreCase(httpMethod) && "/secured/transactions".equals(path)) {
                return handleGetTransactions();
            } else if ("GET".equalsIgnoreCase(httpMethod) && path.startsWith("/secured/transactions/")) {
                String id = path.substring("/secured/transactions/".length());
                return (List<Map<String, Object>>) handleGetTransactionById(id);
            } else {
                throw new IllegalArgumentException("Invalid HTTP method or path.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error handling request: " + e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> handleAddTransaction(Map<String, Object> input) {
        // Obtén el cuerpo como un mapa
        Map<String, Object> body = (Map<String, Object>) input.get("body");

        // Usa Gson para convertir el mapa a un objeto Transaction
        String jsonBody = gson.toJson(body);
        Transaction transaction = gson.fromJson(jsonBody, Transaction.class);

        // Procesa la transacción
        transactionService.addTransaction(transaction);
        boolean isFraudulent = fraudDetectionService.isTransactionFraudulent(transaction);
        transaction.setFraudulent(isFraudulent);

        // Formatea la transacción para la respuesta
        Map<String, Object> formattedTransaction = new HashMap<>();
        formattedTransaction.put("id", transaction.getId());
        formattedTransaction.put("username", transaction.getUsername());
        formattedTransaction.put("sourceAccount", transaction.getSourceAccount());
        formattedTransaction.put("destinationAccount", transaction.getDestinationAccount());
        formattedTransaction.put("amount", transaction.getAmount());
        formattedTransaction.put("date", transaction.getDate());
        formattedTransaction.put("isFraudulent", transaction.isFraudulent());

        // Devuelve la transacción formateada como parte de la lista
        List<Map<String, Object>> response = new ArrayList<>();
        response.add(formattedTransaction);
        return response;
    }


    private List<Map<String, Object>> handleGetTransactions() {
        List<Transaction> transactions = transactionService.getTransactions();
        List<Map<String, Object>> formattedTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            Map<String, Object> formattedTransaction = new HashMap<>();
            formattedTransaction.put("id", transaction.getId());
            formattedTransaction.put("username", transaction.getUsername());
            formattedTransaction.put("sourceAccount", transaction.getSourceAccount());
            formattedTransaction.put("destinationAccount", transaction.getDestinationAccount());
            formattedTransaction.put("amount", transaction.getAmount());
            formattedTransaction.put("date", transaction.getDate());
            formattedTransaction.put("isFraudulent", transaction.isFraudulent());
            formattedTransactions.add(formattedTransaction);
        }

        return formattedTransactions;
    }

    private Map<String, Object> handleGetTransactionById(String id) {
        Transaction transaction = transactionService.getTransactionById(id);
        if (transaction!= null) {
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 200);
            response.put("body", gson.toJson(transaction));
            return response;
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("statusCode", 404);
            response.put("body", "Transaction not found");
            return response;
        }
    }
}
