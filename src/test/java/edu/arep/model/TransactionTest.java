package edu.arep.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest {

    @Test
    public void testTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId("123456789");
        transaction.setSourceAccount("source");
        transaction.setDestinationAccount("destination");
        transaction.setAmount(100.0);
        transaction.setDate("2024-05-26");
        transaction.setFraudulent(false);

        assertEquals("123456789", transaction.getId());
        assertEquals("source", transaction.getSourceAccount());
        assertEquals("destination", transaction.getDestinationAccount());
        assertEquals(100.0, transaction.getAmount());
        assertEquals("2024-05-26", transaction.getDate());
        assertFalse(transaction.isFraudulent());
    }
}
