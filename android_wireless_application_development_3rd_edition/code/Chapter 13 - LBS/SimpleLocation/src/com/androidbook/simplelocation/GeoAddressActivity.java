package com.androidbook.simplelocation;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GeoAddressActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geoaddress);

        final EditText name = (EditText) findViewById(R.id.placename);
        final Geocoder coder = new Geocoder(getApplicationContext());
        final TextView results = (TextView) findViewById(R.id.result);
        final Button map = (Button) findViewById(R.id.map);

        Button geocode = (Button) findViewById(R.id.geocode);
        geocode.setOnClickListener(new View.OnClickListener() {

public void onClick(View v) {
    if (Geocoder.isPresent()) {
        String placeName = name.getText().toString();

        try {
            List<Address> geocodeResults = coder
                    .getFromLocationName(placeName, 3);

            StringBuilder locInfo = new StringBuilder("Results:\n");
            double lat = 0f;
            double lon = 0f;

            for (Address loc : geocodeResults) {
                lat = loc.getLatitude();
                lon = loc.getLongitude();
                locInfo.append("Location: ").append(lat)
                        .append(", ").append(lon).append("\n");
            }

            results.setText(locInfo);

            final String geoURI = String.format("geo:%f,%f", lat,
                    lon);

            map.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Uri geo = Uri.parse(geoURI);
                    Intent geoMap = new Intent(Intent.ACTION_VIEW,
                            geo);
                    startActivity(geoMap);
                }

            });
            map.setVisibility(View.VISIBLE);

        } catch (IOException e) {
            Log.e("GeoAddress", "Failed to get location info", e);
        }
    } else {
        Toast.makeText(GeoAddressActivity.this,
                "No geocoding available", Toast.LENGTH_LONG).show();
    }

}

        });
    }

}
