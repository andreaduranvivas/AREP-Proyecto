package edu.arep.persistence;

import com.mongodb.client.MongoCollection;
import edu.arep.model.Transaction;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class TransactionPersistence {

    @Inject
    MongoConnection client;

    public void insertTransaction(Transaction transaction) {
        Document doc = new Document("username", transaction.getUsername())
                .append("sourceAccount", transaction.getSourceAccount())
                .append("destinationAccount", transaction.getDestinationAccount())
                .append("amount", transaction.getAmount())
                .append("date", transaction.getDate())
                .append("isFraudulent", transaction.isFraudulent());
        client.getCollection().insertOne(doc);
    }

    public Transaction getTransactionById(String id) {
        MongoCollection<Document> collection = client.getCollection();
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            return createTransaction(doc);
        }
        return null;
    }

    public List<Transaction> getAllTransactions() {
        MongoCollection<Document> collection = client.getCollection();
        List<Transaction> transactions = new ArrayList<>();
        for (Document doc : collection.find()) {
            transactions.add(createTransaction(doc));
        }
        return transactions;
    }

    public Transaction createTransaction(Document doc){
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
