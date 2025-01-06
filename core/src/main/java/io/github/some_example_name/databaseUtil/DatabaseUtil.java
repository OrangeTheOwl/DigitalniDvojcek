package io.github.some_example_name.databaseUtil;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import io.github.some_example_name.common.Secrets;

public class DatabaseUtil {
    public static void main( String[] args ) {

        // Replace the placeholder with your MongoDB deployment's connection string
        String uri = Secrets.DatabaseKey;

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("website");
            MongoCollection<Document> collection = database.getCollection("airports");

            Document doc = collection.find(eq("name", "Letališče 1")).first();
            if (doc != null) {
                System.out.println(doc.toJson());
            } else {
                System.out.println("No matching documents found.");
            }
        }
    }

}

