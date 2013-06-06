package com.example.location;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import com.example.service.SendLocationAsyncTask;
import com.google.gson.Gson;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class CurrentLocationProvider extends LocationListenerAdapter {
    public static final String LOCATION = "LOCATION";
    public static final int MIN_TIME = 1000 * 30;
    public static final int MIN_DISTANCE = 5;
    private Context context;

    public CurrentLocationProvider(Context context) {
        this.context = context;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        setCurrentBestLocation(locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false)));
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        locationManager.requestLocationUpdates(GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        setCurrentBestLocation(location);
        String imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        new SendLocationAsyncTask().execute(location, imei);
    }

    void setCurrentBestLocation(Location location) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(LOCATION, new Gson().toJson(location));
        editor.commit();
    }

}
