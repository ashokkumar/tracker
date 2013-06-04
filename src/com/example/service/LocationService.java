package com.example.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class LocationService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();        
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        long timeFromNow = SystemClock.elapsedRealtime() + 1000 * 30 * 1;
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeFromNow, 1000 * 30 * 1, PendingIntent.getBroadcast(this, 0, new Intent("com.apb.beacon.LOCATION_UPDATE_ACTION"), FLAG_UPDATE_CURRENT));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
