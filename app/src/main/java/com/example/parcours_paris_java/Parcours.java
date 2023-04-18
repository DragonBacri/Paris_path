package com.example.parcours_paris_java;

import android.os.Parcel;
import android.os.Parcelable;

import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class Parcours implements Serializable {

    private String name;
    private String description;
    private GeoPoint location;
    private int duration ;

    private List<Questions> questionsList;

    public Parcours(String name, String description, GeoPoint location, int duration, List<Questions> questionsList) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.questionsList = questionsList;
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

    public List<Questions> getQuestionsList() {
        return questionsList;
    }

    public int getDuration() {
        return duration;
    }
}
