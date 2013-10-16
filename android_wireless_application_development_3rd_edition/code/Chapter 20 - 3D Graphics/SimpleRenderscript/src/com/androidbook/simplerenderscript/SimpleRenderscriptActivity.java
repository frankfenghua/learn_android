package com.androidbook.simplerenderscript;

import android.app.Activity;
import android.os.Bundle;

public class SimpleRenderscriptActivity extends Activity {
    private FallingSnowView snowView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snowView = new FallingSnowView(this);
        setContentView(snowView);

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        snowView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        snowView.pause();
    }

}