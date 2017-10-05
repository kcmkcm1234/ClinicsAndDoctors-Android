package com.clinicsanddoctors;

import android.location.Location;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Daro on 08/08/2017.
 */

public class ClinicsApplication extends MultiDexApplication {

    public static String FIREBASE_TOKEN = "";
    private static ClinicsApplication sInstance;
    private Location currentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath(getResources().getString(R.string.font_compact_avenir_medium))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static ClinicsApplication getInstance() {
        if (sInstance != null)
            return sInstance;
        else
            return sInstance = new ClinicsApplication();
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }
}
