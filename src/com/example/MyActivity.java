package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.example.service.LocationService;

public class MyActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startService(new Intent(this, LocationService.class));
    }
}
