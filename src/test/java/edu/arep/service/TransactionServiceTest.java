package edu.arep.service;

import edu.arep.model.Transaction;
import edu.arep.persistence.TransactionPersistence;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @Mock
    private TransactionPersistence transactionPersistence;

    @InjectMocks
    private TransactionService transactionService;

    public TransactionServiceTest() {
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

        doNothing().when(transactionPersistence).insertTransaction(transaction);

        transactionService.addTransaction(transaction);

        verify(transactionPersistence, times(1)).insertTransaction(transaction);
    }

    @Test
    public void testGetTransactions() {
        Transaction transaction = new Transaction();
        transaction.setSourceAccount("source");
        transaction.setDestinationAccount("destination");
        transaction.setAmount(100.0);
        transaction.setDate("2024-05-26");
        transaction.setFraudulent(false);

        when(transactionPersistence.getAllTransactions()).thenReturn(Collections.singletonList(transaction));

        List<Transaction> transactions = transactionService.getTransactions();

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

        when(transactionPersistence.getTransactionById(id)).thenReturn(transaction);

        Transaction result = transactionService.getTransactionById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("source", result.getSourceAccount());
        assertEquals("destination", result.getDestinationAccount());
        assertEquals(100.0, result.getAmount());
        assertEquals("2024-05-26", result.getDate());
        assertFalse(result.isFraudulent());
    }
}
