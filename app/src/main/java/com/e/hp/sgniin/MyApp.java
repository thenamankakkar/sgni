package com.e.hp.sgniin;

import android.app.Application;

import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        InternetAvailabilityChecker.init(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        InternetAvailabilityChecker.getInstance().removeAllInternetConnectivityChangeListeners();
    }
}