package com.softonitg.jorge.softondemo.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.softonitg.jorge.softondemo.Interfaces.OnSearchStartedListener;
import com.softonitg.jorge.softondemo.R;

/**
 *
 */
public class SearchFragment extends Fragment {

    private Button searchButton;
    private EditText searchCriteria;

    private final String LOG_TAG = "SEARCH FRAGMENT LOG TAG";

    private OnSearchStartedListener onSearchStartedListener;



    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        searchButton = (Button) v.findViewById(R.id.search_button);
        searchCriteria = (EditText) v.findViewById(R.id.search_edit_text);
        setClickListeners();
        return v;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onSearchStartedListener = (OnSearchStartedListener) activity;
        } catch (ClassCastException e) {
            Log.i(LOG_TAG, "Parent class must implement interface");
            throw new ClassCastException(activity.toString()
                    + " must implement " + OnSearchStartedListener.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onSearchStartedListener = null;
    }


    private void setClickListeners() {
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
        Log.i(LOG_TAG, "Starting search from SEARCH FRAGMENT");
    }


}
