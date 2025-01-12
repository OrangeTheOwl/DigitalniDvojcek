package com.example.airportmobile.database


import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


class MongoDBConnection {
    private val CONNECTION_STRING = "mongodb+srv://rri:<db_password>@cluster0.1aaw6qs.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

    fun getMongoClient(): MongoClient {
        return MongoClients.create(CONNECTION_STRING)
    }
}