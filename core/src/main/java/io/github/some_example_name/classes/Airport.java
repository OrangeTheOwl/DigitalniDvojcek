package io.github.some_example_name.classes;

import io.github.some_example_name.utils.Geolocation;

public class Airport {
    public Geolocation location;
    public String country;
    public String address;

    public Airport(Double lon, Double lat, String country, String address){
        location = new Geolocation(lat, lon);
        this.address = address;
        this.country = country;
    }

}
