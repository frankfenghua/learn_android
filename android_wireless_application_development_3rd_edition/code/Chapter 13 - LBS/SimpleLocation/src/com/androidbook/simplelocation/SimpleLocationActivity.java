package com.androidbook.simplelocation;

public class SimpleLocationActivity extends MenuActivity {
    @Override
    void prepareMenu() {

        addMenuItem("1. GPS Sample", GPSActivity.class);
        addMenuItem("2. Geocode Sample", GeoAddressActivity.class);
        addMenuItem("3. Mapping Sample",MappingActivity.class);
        addMenuItem("4. Show Huts Sample",ShowHutsActivity.class);

    }
}