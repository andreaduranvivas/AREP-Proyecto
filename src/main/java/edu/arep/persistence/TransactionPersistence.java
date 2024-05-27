package edu.arep.persistence;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import edu.arep.model.Transaction;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class TransactionPersistence {

    private final MongoConnection mongoConnection = new MongoConnection();

    public void insertTransaction(Transaction transaction) {
        Document doc = new Document("username", transaction.getUsername())
                .append("sourceAccount", transaction.getSourceAccount())
                .append("destinationAccount", transaction.getDestinationAccount())
                .append("amount", transaction.getAmount())
                .append("date", transaction.getDate())
                .append("isFraudulent", transaction.isFraudulent());
        mongoConnection.getCollection().insertOne(doc);
    }

    public Transaction getTransactionById(String id) {
        MongoCollection<Document> collection = mongoConnection.getCollection();
        Document doc = collection.find(new Document("_id", new ObjectId(id))).first();
        return createTransaction(doc);
    }

    public List<Transaction> getAllTransactions() {
        MongoCollection<Document> collection = mongoConnection.getCollection();
        List<Transaction> transactions = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                transactions.add(createTransaction(doc));
            }
        }
        return transactions;
    }

    private Transaction createTransaction(Document doc) {
        if (doc == null) return null;
        Transaction transaction = new Transaction();
        transaction.setId(doc.getObjectId("_id").toString());
        transaction.setUsername(doc.getString("username"));
        transaction.setSourceAccount(doc.getString("sourceAccount"));
        transaction.setDestinationAccount(doc.getString("destinationAccount"));
        transaction.setAmount(doc.getDouble("amount"));
        transaction.setDate(doc.getString("date"));
        transaction.setFraudulent(doc.getBoolean("isFraudulent"));
        return transaction;
    }
}
