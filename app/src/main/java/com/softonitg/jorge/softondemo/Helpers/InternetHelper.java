package com.softonitg.jorge.softondemo.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Jorge on 16/05/2015.
 * Used to verify internet connection
 */
public class InternetHelper {

    private static String LOG_TAG = "Internet Helper";

    public static boolean verifyInternetAccess(Context context){
        boolean hasAccess = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isConnected())){
            hasAccess = true;
            Log.i(LOG_TAG, "Internet connection available");
        }
        else {
            Log.i(LOG_TAG, "Internet connection unavailable");
        }
        return hasAccess;
    }
}
