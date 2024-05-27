package edu.arep.service;

import edu.arep.model.Transaction;
import edu.arep.persistence.TransactionPersistence;

import java.util.List;

public class TransactionService {

    private final TransactionPersistence transactionPersistence = new TransactionPersistence();

    public void addTransaction(Transaction transaction) {
        transactionPersistence.insertTransaction(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactionPersistence.getAllTransactions();
    }

    public Transaction getTransactionById(String id) {
        return transactionPersistence.getTransactionById(id);
    }
}
