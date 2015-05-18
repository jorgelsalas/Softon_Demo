package com.softonitg.jorge.softondemo;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Jorge on 17/05/2015.
 * Used to leverage Calligraphy library
 */
public class SoftonITGApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        String font = "fonts/PTS76F.ttf";
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath(font)
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

}
