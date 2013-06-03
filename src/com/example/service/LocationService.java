package com.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class LocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("Service started");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
