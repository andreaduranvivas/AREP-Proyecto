package edu.arep.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoConnection {

    private final MongoClient client;

    public MongoConnection() {
        this.client = MongoClients.create("mongodb+srv://briancfajardo:admin@cluster0.jyodi8s.mongodb.net/?retryWrites=true&w=majority");
    }

    public MongoCollection<Document> getCollection() {
        MongoDatabase database = client.getDatabase("FraudDetection");
        return database.getCollection("Frauds");
    }
}
