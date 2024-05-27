package edu.arep.model;

import lombok.Data;

import java.util.Map;

@Data
public class User {
    private String id;
    private String username;
    private String password;

    private Map<String, Transaction> transactions;
}
