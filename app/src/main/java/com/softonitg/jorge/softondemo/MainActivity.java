package com.softonitg.jorge.softondemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.softonitg.jorge.softondemo.AsyncTasks.VenueSearchTask;
import com.softonitg.jorge.softondemo.Entities.FourSquareVenue;
import com.softonitg.jorge.softondemo.Entities.SimpleMarker;
import com.softonitg.jorge.softondemo.Fragments.CustomMapFragment;
import com.softonitg.jorge.softondemo.Fragments.Dialogs.AboutMeDialogFragment;
import com.softonitg.jorge.softondemo.Fragments.Dialogs.LogOutVerificationDialogFragment;
import com.softonitg.jorge.softondemo.Fragments.SearchFragment;
import com.softonitg.jorge.softondemo.Helpers.GoogleServicesHelper;
import com.softonitg.jorge.softondemo.Helpers.InternetHelper;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.softonitg.jorge.softondemo.Helpers.LocationManagerHelper;
import com.softonitg.jorge.softondemo.Interfaces.LogOutListener;
import com.softonitg.jorge.softondemo.Interfaces.OnSearchStartedListener;
import com.softonitg.jorge.softondemo.Interfaces.OnVenueSearchCompletedListener;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends ActionBarActivity implements LogOutListener, OnVenueSearchCompletedListener,
        ConnectionCallbacks ,OnConnectionFailedListener, OnSearchStartedListener {

    private GoogleServicesHelper googleServicesHelper;
    private LocationManagerHelper locationManagerHelper;
    private Location lastLocation = null;
    private FragmentManager fm;
    private final String LOG_TAG = "MainActivity TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        if (findViewById(R.id.fragment_container) != null){
            if (savedInstanceState == null){
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(getIntent().getExtras());
                fm.beginTransaction().
                        add(R.id.fragment_container, searchFragment, getString(R.string.search_fragment_id)).
                        commit();
            }
        }

        googleServicesHelper = new GoogleServicesHelper(this);
        locationManagerHelper = new LocationManagerHelper(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about_me) {
            showAboutMeDialog();
            return true;
        }
        else if (id == android.R.id.home){
            handleBackPress();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAboutMeDialog(){
        AboutMeDialogFragment dialog = new AboutMeDialogFragment();
        dialog.show(fm, getString(R.string.about_me_fragment_id));
    }

    private void showLogOutVerificationDialog(){
        LogOutVerificationDialogFragment dialog = new LogOutVerificationDialogFragment();
        dialog.show(fm, getString(R.string.log_out_verification_fragment_id));
    }

    private void logOut(){
        Intent login = new Intent(this, LoginActivity.class);
        login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(login);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                handleBackPress();
                return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void handleBackPress(){
        SearchFragment searchFragment = (SearchFragment) fm.findFragmentByTag(getString(R.string.search_fragment_id));
        if(searchFragment != null && searchFragment.isVisible()){
            //Current fragment is search fragment so we show Log out confirmation
            showLogOutVerificationDialog();
        }
        else {
            //Return to search fragment
            searchFragment = new SearchFragment();
            fm.beginTransaction().
                    replace(R.id.fragment_container, searchFragment, getString(R.string.search_fragment_id)).
                    commit();
        }
    }


    @Override
    public void onLogOutConfirmed() {
        finish();
        logOut();
    }

    private void startFourSquareSearch(String location){
        //Hide Keyboard
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        catch (NullPointerException e){
            Log.e(LOG_TAG, getString(R.string.get_current_focus_returned_null_warning));
        }


        if (InternetHelper.verifyInternetAccess(this)){
            //We have Internet connection and can perform a search

            if (!location.equalsIgnoreCase("")){
                //User has provided input so we get the devices location
                lastLocation = locationManagerHelper.getLastKnownLocation();
                if (lastLocation == null && googleServicesHelper.isConnectionAvailable()){
                    //Location Manager could not acquire last location
                    //Falling back to google services
                    lastLocation = googleServicesHelper.getLastLocation();
                }
                //Verify if still null after querying google services
                if (lastLocation != null){
                    //We have a location so we can query FourSquare servers!
                    Log.i(LOG_TAG, getString(R.string.perform_query_alert));
                    new VenueSearchTask(location, lastLocation.getLatitude(),lastLocation.getLongitude(), this).execute("");
                }
                else {
                    //Location still null, so we warn the user
                    Toast.makeText(this, R.string.location_not_acquired_warning, Toast.LENGTH_LONG).show();
                    Toast.makeText(this, R.string.verify_gps_and_network_warning, Toast.LENGTH_LONG).show();
                }
            }
            else {
                //User has left field blank so we ask to him to input data
                Toast.makeText(this, R.string.main_empty_search_warning, Toast.LENGTH_LONG).show();
            }
        }
        else {
            //We don't have Internet
            Toast.makeText(this, R.string.internet_not_available_warning, Toast.LENGTH_LONG).show();
        }
    }



    private void addMapMarkers(FourSquareVenue searchedLocation){
        if(searchedLocation != null && lastLocation != null){
            ArrayList<SimpleMarker> markers = new ArrayList<>();
            markers.add(new SimpleMarker(new LatLng(searchedLocation.getLatitude(), searchedLocation.getLongitude()),
                    searchedLocation.getName()));
            markers.add(new SimpleMarker(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()),
                    getString(R.string.user_location_marker_title)));
            String distance = String.valueOf(searchedLocation.getDistanceToLocation());

            CustomMapFragment customMapFragment = (CustomMapFragment) fm.findFragmentByTag(getString(R.string.map_fragment_id));
            if(customMapFragment != null && customMapFragment.isVisible()){
                //Current fragment is map fragment so we update UI
                customMapFragment.placeMarkers(markers);
                customMapFragment.updateDistance(distance);
                Log.e(LOG_TAG, "Map Fragment was already visible");
            }
            else {
                //Ceate new Map Fragment and display it
                String json = new Gson().toJson(markers);
                Log.e(LOG_TAG, "Markers JSON has: " + json);
                customMapFragment = CustomMapFragment.newInstance(json, distance);
                fm.beginTransaction().
                        replace(R.id.fragment_container, customMapFragment, getString(R.string.map_fragment_id)).
                        addToBackStack(null).commit();
                Log.e(LOG_TAG, "Creating a new Map Fragment");
            }
            Log.w(LOG_TAG, "Latitude: " + searchedLocation.getLatitude());
            Log.w(LOG_TAG, "Longitude: " + searchedLocation.getLongitude());
            Log.w(LOG_TAG, "Name: " + searchedLocation.getName());
            Log.w(LOG_TAG, "Distance: " + searchedLocation.getDistanceToLocation());
        }
        else {
            Toast.makeText(this, getString(R.string.unable_to_add_markers_warning), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onDataReceived(ArrayList<FourSquareVenue> venues) {
        addMapMarkers(venues.get(0));
    }

    @Override
    public void onDataReceivedFailure() {
        //Something went wrong while querying FourSquare servers
        Toast.makeText(this, R.string.foursquare_error_warning, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDataReceivedWithGeocodeError() {
        //The location supplied was not valid
        Toast.makeText(this, R.string.geocode_error_warning, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onEmptyDataSetReceived() {
        //Notify user that no venues were retrieved
        Toast.makeText(this, R.string.no_venues_retrieved_from_web_warning, Toast.LENGTH_LONG).show();
    }



    @Override
    public void onConnected(Bundle arg0) {
        Log.i(LOG_TAG, getString(R.string.connected_to_google_services));
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        Log.e(LOG_TAG, getString(R.string.connection_to_google_services_suspended));
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        Log.e(LOG_TAG, getString(R.string.connection_to_google_services_failed));
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleServicesHelper.Disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleServicesHelper.Disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleServicesHelper.connect();
    }

    @Override
    public void onSearchStartedFromFragment(String location) {
        startFourSquareSearch(location);
    }


}
