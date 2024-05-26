package edu.arep.controller;

import edu.arep.model.Transaction;
import edu.arep.service.TransactionService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    public TransactionControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTransaction() {
        Transaction transaction = new Transaction();
        transaction.setSourceAccount("source");
        transaction.setDestinationAccount("destination");
        transaction.setAmount(100.0);
        transaction.setDate("2024-05-26");
        transaction.setFraudulent(false);

        doNothing().when(transactionService).addTransaction(transaction);

        Response response = transactionController.addTransaction(transaction);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(transactionService, times(1)).addTransaction(transaction);
    }

    @Test
    public void testGetTransactions() {
        Transaction transaction = new Transaction();
        transaction.setSourceAccount("source");
        transaction.setDestinationAccount("destination");
        transaction.setAmount(100.0);
        transaction.setDate("2024-05-26");
        transaction.setFraudulent(false);

        when(transactionService.getTransactions()).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionController.getTransactions();

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals("source", transactions.get(0).getSourceAccount());
    }

    @Test
    public void testGetTransactionById() {
        String id = "1234567890abcdef12345678";
        Transaction transaction = new Transaction();
        transaction.setId(id);
        transaction.setSourceAccount("source");
        transaction.setDestinationAccount("destination");
        transaction.setAmount(100.0);
        transaction.setDate("2024-05-26");
        transaction.setFraudulent(false);

        when(transactionService.getTransactionById(id)).thenReturn(transaction);

        Transaction result = transactionController.getTransactionById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("source", result.getSourceAccount());
        assertEquals("destination", result.getDestinationAccount());
        assertEquals(100.0, result.getAmount());
        assertEquals("2024-05-26", result.getDate());
        assertFalse(result.isFraudulent());
    }
}
