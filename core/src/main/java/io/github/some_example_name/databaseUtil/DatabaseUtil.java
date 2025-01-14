package io.github.some_example_name.databaseUtil;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.some_example_name.classes.Airport;
import io.github.some_example_name.common.Secrets;
import io.github.some_example_name.utils.Geocoding;

public class DatabaseUtil {

    static String uri = Secrets.KEY;


    public static List<Airport> getAirportLocations(){
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("website");
            MongoCollection<Document> collection = database.getCollection("locations");
            DistinctIterable<String> doc = collection.distinct("address", String.class);

            for (String document : doc) {
                System.out.println(document);
            }

            List<Airport> allAirports = new ArrayList<Airport>();

            for (String document : doc) {
                Airport test = Geocoding.getLongLat(document);
                allAirports.add(test);
            }

            return allAirports;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }

}

