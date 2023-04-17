package com.example.revuproject.database.relations;

public class PlaceCoordinates {
    public double latitude;
    public double longitude;

    public PlaceCoordinates(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public PlaceCoordinates(String string){
        String[] split = string.split(", ");
        this.latitude = Double.parseDouble(split[0]);
        this.longitude = Double.parseDouble(split[1]);
    }
}
