package edu.arep.service;

import edu.arep.model.Transaction;
import edu.arep.persistence.TransactionPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TransactionService {

    @Inject
    TransactionPersistence transactionPersistence;

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
