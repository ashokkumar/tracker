package com.example.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

public class LocationUpdateReceiver extends BroadcastReceiver {
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Location location = new CurrentLocationProvider(context).getLocation();
        System.out.println("Location :- " +  location.getLatitude() + " - " + location.getLongitude());
    }

}
