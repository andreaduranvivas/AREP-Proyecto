package edu.arep.persistence;

import com.mongodb.client.MongoCollection;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class MongoConnectionTest {

    @Inject
    MongoConnection mongoConnection;

    @Test
    void testMongoConnection() {
        MongoCollection<Document> collection = mongoConnection.getCollection();
        assertNotNull(collection, "La colección no debe ser nula");
    }
}
