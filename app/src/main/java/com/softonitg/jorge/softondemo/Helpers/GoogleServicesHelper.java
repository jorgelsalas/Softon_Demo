package com.softonitg.jorge.softondemo.Helpers;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Jorge on 16/05/2015.
 * Google Services helper class to handle connections and
 * acquire location.
 */
public class GoogleServicesHelper {

    private Activity activity;
    private GoogleApiClient mGoogleApiClient;
    private final static String LOG_TAG = GoogleServicesHelper.class.getSimpleName();

    public GoogleServicesHelper() {

    }

    public GoogleServicesHelper(Activity activity) {
        this.activity = activity;
        buildGoogleApiClient();
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) activity)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) activity)
                .addApi(LocationServices.API)
                .build();
        connect();
    }

    public Location getLastLocation(){
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(lastLocation == null){
            Log.e(LOG_TAG, "Google Services returned null location");
        }
        return lastLocation;
    }

    public boolean isConnectionAvailable(){
        return mGoogleApiClient.isConnected();
    }

    public void Disconnect(){
        if (mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public void connect(){
        if (!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
    }

}
