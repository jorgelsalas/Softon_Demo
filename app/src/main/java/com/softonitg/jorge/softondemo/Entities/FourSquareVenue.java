package com.softonitg.jorge.softondemo.Entities;

/**
 * Created by Jorge on 16/05/2015.
 * This is a simple POJO Class to encapsulate a FourSquare venue
 */
public class FourSquareVenue {

    public FourSquareVenue(int result_number, String name, double latitude,
                           double longitude, double distanceToLocation) {
        this.result_number = result_number;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceToLocation = distanceToLocation;
    }


    private int result_number;
    private String name;
    private double latitude;
    private double longitude;
    private double distanceToLocation;


    public FourSquareVenue() {

    }

    public int getResult_number() {
        return result_number;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistanceToLocation() {
        return distanceToLocation;
    }

    public void setResult_number(int result_number) {
        this.result_number = result_number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDistanceToLocation(double distanceToLocation) {
        this.distanceToLocation = distanceToLocation;
    }

    public String getLatAndLong(){
        return getLatitude() + "," + getLongitude();
    }
}
