package io.github.some_example_name.utils;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import io.github.some_example_name.classes.Airport;
import io.github.some_example_name.classes.Traffic;

public class Geocoding {
    static String mapServiceUrl = "https://maps.geoapify.com/v1/geocode/search?text=";
    static String token = "&apiKey=" + Keys.GEOAPIFY;
    static String format = "&format=json";


    public static Airport getLongLat(String address) throws IOException, InterruptedException {

        address = encodeAddress(address);
        String uri = mapServiceUrl + address + format + token;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header("Content-Type", "application/json")
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println(response.body());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body());
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaa"+jsonNode.findValue("lat"));
        Double lat = jsonNode.findValue("lat").asDouble();
        Double lon = jsonNode.findValue("lon").asDouble();
        String country = jsonNode.findValue("country").asText();
/*


        System.out.println(jsonNode.findValue("lon"));
        System.out.println(jsonNode.findValue("lat"));
        System.out.println(jsonNode.findValue("country"));
        System.out.println(jsonNode.findValue("address_line1"));
*/

        return new Airport(lon, lat,country, address);
    }

    public static Traffic getLongLatTraffic(String address) throws IOException, InterruptedException {

        address = encodeAddress(address);
        String uri = mapServiceUrl + address + format + token;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header("Content-Type", "application/json")
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

        //System.out.println(response.body());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.body());
        //System.out.println("aaaaaaaaaaaaaaaaaaaaaa"+jsonNode.findValue("lat"));
        Double lat = jsonNode.findValue("lat").asDouble();
        Double lon = jsonNode.findValue("lon").asDouble();

        return new Traffic(lon, lat);
    }

    public static String encodeAddress(String addres) throws UnsupportedEncodingException {
        return URLEncoder.encode(addres, StandardCharsets.UTF_8.toString());
    }
}
