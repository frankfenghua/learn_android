package com.androidbook.tracks;

import java.util.Random;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidbook.gpx.TrackPointProvider;

public class RefreshTracksActivity extends Activity {
    private static final String DEBUG_TAG = "Tracks";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testDeleteAllTP();
        
        int i = 15;
        while (i-- > 0) {
        	testAddTP();
        }

		setContentView(R.layout.main);
		
		TextView textcontrol = (TextView) findViewById(R.id.tracktext);
		textcontrol.setText("Tracking Values Refreshed");
    }
    private void testDeleteAllTP() {
        /*
         * int rows = getContentResolver().delete(
         * TrackPointProvider.CONTENT_URI, TrackPointProvider.ELEVATION+ "=?",
         * new String[] { "-6" });
         */
        int rows = getContentResolver().delete(TrackPointProvider.CONTENT_URI, null, null);
        Log.d(DEBUG_TAG, "Rows: " + rows);
    }

    private void testAddTP() {
        String type = getContentResolver().getType(TrackPointProvider.CONTENT_URI);
        Log.d(DEBUG_TAG, "Type: " + type);
        // randomize for the live folder display
        Random rand = new Random();
        int year = rand.nextInt(300) + 1900;
        int month = rand.nextInt(12) +1;
        int day = rand.nextInt(28) + 1;
        int hour = rand.nextInt(24);
        int min = rand.nextInt(60);
        int sec = rand.nextInt(60);
        ContentValues values = new ContentValues();
        String timestamp = String.format("%04d-%02d-%02dT%02d-%02d-%02dZ", year, month, day, hour, min, sec);
        values.put(TrackPointProvider.TIMESTAMP, timestamp);
        values.put(TrackPointProvider.LATITUDE, "43.94996");
        values.put(TrackPointProvider.LONGITUDE, "-71.08341");
        values.put(TrackPointProvider.ELEVATION, "-12");
        Uri newTP = getContentResolver().insert(TrackPointProvider.CONTENT_URI, values);
        Log.d(DEBUG_TAG, "URI: " + newTP.toString());
    }

}
