package io.github.some_example_name.classes;

import io.github.some_example_name.utils.Geolocation;

public class Traffic {
    public Geolocation location;
    public String event;

    public Traffic(Double lon, Double lat/*, String event*/){
        location = new Geolocation(lat, lon);
        /*this.event = event;*/
    }
}
