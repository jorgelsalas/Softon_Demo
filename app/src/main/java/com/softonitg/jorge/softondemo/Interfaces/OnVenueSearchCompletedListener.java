package com.softonitg.jorge.softondemo.Interfaces;

import com.softonitg.jorge.softondemo.Entities.FourSquareVenue;

import java.util.ArrayList;

/**
 * Created by Jorge on 16/05/2015.
 * Used to communicate the AsyncTask that queries FourSquare with the parent activity.
 */
public interface OnVenueSearchCompletedListener {

    void onDataReceived(ArrayList<FourSquareVenue> venues);
    void onDataReceivedFailure();
    void onEmptyDataSetReceived();
    void onDataReceivedWithGeocodeError();


}
