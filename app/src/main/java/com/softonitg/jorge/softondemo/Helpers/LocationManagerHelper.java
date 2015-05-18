package com.softonitg.jorge.softondemo.Helpers;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

/**
 * Created by Jorge on 16/05/2015.
 * Used to acquire location through Location System Service
 */
public class LocationManagerHelper {

    private LocationManager locationManager;
    private String provider;
    private static final String LOG_TAG = LocationManagerHelper.class.getSimpleName();

    public LocationManagerHelper(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Activity.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = locationManager.getBestProvider(criteria, true);
    }

    public Location getLastKnownLocation(){
        Location location = locationManager.getLastKnownLocation(provider);
        if(location == null){
            Log.e(LOG_TAG, "Location currently unavailable");
        }
        return location;
    }

}
