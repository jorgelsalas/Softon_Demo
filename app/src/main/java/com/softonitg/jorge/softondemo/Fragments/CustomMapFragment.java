package com.softonitg.jorge.softondemo.Fragments;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.softonitg.jorge.softondemo.Entities.SimpleMarker;
import com.softonitg.jorge.softondemo.Interfaces.OnSearchStartedListener;
import com.softonitg.jorge.softondemo.R;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class CustomMapFragment extends Fragment implements OnMapReadyCallback {

    private final String LOG_TAG = "MAP FRAGMENT LOG TAG";
    private EditText searchCriteria;
    private Button searchButton;
    private TextView distanceLabel;
    private GoogleMap map = null;
    private String distance;
    private ArrayList<SimpleMarker> markers = new ArrayList<>();
    private SupportMapFragment mapFragment;
    private FragmentManager fragmentManager;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String MARKERS_BUNDLE_KEY = "markers_key";
    public static final String DISTANCE_BUNDLE_KEY = "distance_key";
    private OnSearchStartedListener onSearchStartedListener;




    public static CustomMapFragment newInstance(String markersJson, String distance) {
        CustomMapFragment fragment = new CustomMapFragment();
        Bundle args = new Bundle();
        args.putString(MARKERS_BUNDLE_KEY, markersJson);
        Log.e("MAP FRAGMENT LOG TAG", "Markers Json in fragment creator has: " + markersJson);
        args.putString(DISTANCE_BUNDLE_KEY, distance);
        Log.e("MAP FRAGMENT LOG TAG", "Distance in fragment creator has: " + distance);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String json = getArguments().getString(MARKERS_BUNDLE_KEY);
            Type arrayListOfSimpleMarkers = new TypeToken<ArrayList<SimpleMarker>>(){}.getType();
            markers = new Gson().fromJson(json, arrayListOfSimpleMarkers);
            distance = getArguments().getString(DISTANCE_BUNDLE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        searchButton = (Button) v.findViewById(R.id.map_fragment_search_button);
        searchCriteria = (EditText) v.findViewById(R.id.map_fragment_edit_text);
        distanceLabel = (TextView) v.findViewById(R.id.map_fragment_distance_tv);

        fragmentManager = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment != null){
            mapFragment.getMapAsync(this);
        }
        else{
            Log.e(LOG_TAG, "Map Fragment not available!!!!");
        }

        setClickListeners();

        //Use gson to retrieve venues
        if (savedInstanceState != null){
            String json = savedInstanceState.getString(MARKERS_BUNDLE_KEY);
            if(json != null){
                Type arrayListOfSimpleMarkers = new TypeToken<ArrayList<SimpleMarker>>(){}.getType();
                markers = new Gson().fromJson(json, arrayListOfSimpleMarkers);
                if (markers != null && markers.size() > 0){
                    placeMarkers(markers);
                }
            }
            distance = savedInstanceState.getString(DISTANCE_BUNDLE_KEY);
            if(distance != null){
                updateDistance(distance);
            }
        }
        else {
            Log.e(LOG_TAG, "Saved instance is NULL!!!!!");
        }


        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Use gson to save markers
        super.onSaveInstanceState(outState);
        if (markers.size() > 0){
            String json = new Gson().toJson(markers);
            outState.putString(MARKERS_BUNDLE_KEY, (json));
        }
        if (distance != null){
            outState.putString(DISTANCE_BUNDLE_KEY, distance);
        }
        super.onSaveInstanceState(outState);
    }

    public void setMarkerList(ArrayList<SimpleMarker> newMarkers){
        markers = newMarkers;
    }

    public void placeMarkers(ArrayList<SimpleMarker> newMarkers){
        Log.e(LOG_TAG, "Placing markers!!!!");
        Log.e(LOG_TAG, "Markers size = " + markers.size());
        setMarkerList(newMarkers);
        if (map != null) {
            map.clear();
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(SimpleMarker marker : newMarkers){
                map.addMarker(new MarkerOptions()
                        .position(marker.getPosition())
                        .title(marker.getTitle()));
                //Include marker position into the bounds builder
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();
            //Moving camera to show all markers
            int padding = 50;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            map.animateCamera(cu);
        }
        else {
            Log.e(LOG_TAG, "Map not available!!!!!");
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onSearchStartedListener = (OnSearchStartedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement " + OnSearchStartedListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSearchStartedListener = null;
    }



    private void setClickListeners(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFourSquareSearch();
            }
        });
    }

    private void startFourSquareSearch(){
        String location = searchCriteria.getText().toString().trim();
        onSearchStartedListener.onSearchStartedFromFragment(location);
        searchCriteria.setText("");
    }


    public void updateDistance(String distance){
        Log.e(LOG_TAG, "Updating Distance!!!!!");
        this.distance = distance;
        distanceLabel.setText(getActivity().getString(R.string.map_tv_distance_text) + distance);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Log.e(LOG_TAG, "MAP IS AVAILABLE NOW!!!!!!");
        if(markers != null){
            placeMarkers(markers);
            updateDistance(distance);
        }
        else {
            Log.e(LOG_TAG, "Markers are null after map is available!!!!!");
        }
    }
}
