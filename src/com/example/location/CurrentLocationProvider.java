package com.example.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class CurrentLocationProvider extends LocationListenerAdapter {
    private Location currentLocation;

    public CurrentLocationProvider(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, 1000 * 30, 0, this);
        locationManager.requestLocationUpdates(GPS_PROVIDER, 1000 * 30, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    public Location getLocation() {
        return currentLocation;
    }
}
