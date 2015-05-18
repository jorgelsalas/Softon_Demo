package com.softonitg.jorge.softondemo.Entities;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jorge on 16/05/2015.
 * This is a simple POJO Class to encapsulate a SimpleMarker
 */
public class SimpleMarker {

    public SimpleMarker(LatLng position, String title) {
        super();
        this.position = position;
        this.title = title;
    }

    private LatLng position;
    private String title;

    public SimpleMarker() {

    }

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
