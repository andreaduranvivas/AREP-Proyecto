package edu.arep.model;

import lombok.Data;

@Data
public class Transaction {
    private String id;
    private String sourceAccount;
    private String destinationAccount;
    private double amount;
    private String date;
    private boolean isFraudulent;
}
