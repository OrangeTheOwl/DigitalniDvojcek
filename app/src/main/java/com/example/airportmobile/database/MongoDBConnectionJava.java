package com.example.airportmobile.database;

import android.util.Log;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;

public class MongoDBConnectionJava {

    // Povezovalni niz za vaš MongoDB Atlas grozd z +srv
    private final String uri = "mongodb+srv://rri:rriDigitalniDvojcek@cluster0.1aaw6qs.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0";

    // MongoDB klient, zbirka in baza podatkov
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    // Konstruktor za inicializacijo povezave
    public MongoDBConnectionJava() {
        try {
            ConnectionString connectionString = new ConnectionString(uri);
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .serverApi(ServerApi.builder()
                            .version(ServerApiVersion.V1)
                            .build())
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("website");
            collection = database.getCollection("crowdData");

        } catch (Exception e) {
            Log.e("MongoDBConnection", "Error initializing MongoDB connection", e);
        }
    }

    // Metoda za pridobitev vseh podatkov iz zbirke
    public List<Document> getAllData() {
        List<Document> result = new ArrayList<>();
        try {
            collection.find().iterator().forEachRemaining(result::add);
        } catch (Exception e) {
            Log.e("MongoDBConnection", "Error fetching data", e);
        }
        return result;
    }

    // Metoda za zaprtje MongoDB povezave
    public void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
