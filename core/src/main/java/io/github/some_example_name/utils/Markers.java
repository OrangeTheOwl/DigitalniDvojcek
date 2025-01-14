package io.github.some_example_name.utils;

import static io.github.some_example_name.utils.MapRasterTiles.fetchTile;
import static io.github.some_example_name.utils.MapRasterTiles.getTexture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import io.github.some_example_name.classes.Airport;

public class Markers {
    static String mapServiceUrl = "https://api.geoapify.com/v1/icon/?type=awesome&color=%230068ff&size=xx-large&icon=plane&scaleFactor=2";
    static String token = "&apiKey=" + Keys.GEOAPIFY;

    public static Texture getMarkerIcon() throws IOException {
        URL url = new URL(mapServiceUrl + token);
        ByteArrayOutputStream bis = fetchTile(url);
        return getTexture(bis.toByteArray());
    }
    /*public static void getMarkerIcon() throws IOException, InterruptedException {
        String uri = mapServiceUrl + token;
        ImageIcon test = new ImageIcon();


        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .header("Content-Type", "application/json")
            .build();

        HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());


        System.out.println(response.body());

    }*/

}
