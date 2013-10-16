package com.androidbook.simpleactionbars;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

public class VacuumActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vac);
        
        ActionBar bar = getActionBar();
        bar.hide();
    }
}