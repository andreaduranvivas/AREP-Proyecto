package edu.arep.persistence;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;

@ApplicationScoped
public class MongoConnection {

    @Inject
    MongoClient client;

    public MongoCollection<Document> getCollection() {
        return client.getDatabase("FraudDetection").getCollection("Frauds");
    }
}
