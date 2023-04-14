package com.example.parcours_paris_java;

import org.osmdroid.util.GeoPoint;

import java.util.List;

public class Parcours {

    private String name;
    private String description;
    private GeoPoint location;
    private int duration ;

    public Parcours(String name, String description, GeoPoint location, int duration) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public int getDuration() {
        return duration;
    }
}
