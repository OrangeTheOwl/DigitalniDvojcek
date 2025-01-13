/*
package com.example.airportmobile.database


import org.bson.Document;
import com.mongodb.kotlin.client.coroutine.MongoClient;
*/
/*
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;*//*



class MongoDBConnection {
    private val CONNECTION_STRING = Secret()

    fun getMongoClient(): MongoClient {
        return MongoClient.create(CONNECTION_STRING.KEY)
    }

}*/
