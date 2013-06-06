package com.example.service;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SendLocationAsyncTask extends AsyncTask{
    @Override
    protected Object doInBackground(Object... params) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://10.16.3.96:3000/gps_tracker");
        Location location = (Location) params[0];
        String imei = (String) params[1];
        try {

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
        nameValuePairs.add(new BasicNameValuePair("imei", imei));
        nameValuePairs.add(new BasicNameValuePair("latitude", location.getLatitude()+""));
        nameValuePairs.add(new BasicNameValuePair("longitude", location.getLongitude()+""));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        HttpResponse response = httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.i("INFO", e.getMessage());
        }

        System.out.print("POSTED SUCCESSFULLY:-----------------------");
        return null;
    }

}
