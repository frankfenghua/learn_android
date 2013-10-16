package com.androidbook.simpleactionbars;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class SweepActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sweep);
        
        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {    
        switch (item.getItemId()) 
        {        
            case android.R.id.home:            
                // special case: app icon in Action Bar clicked; go to launch activity            
                Intent intent = new Intent(this, SimpleActionBarsActivity.class);            
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);          // clear the activity stack  
                startActivity(intent);            
                return true;        
            default:            
                return super.onOptionsItemSelected(item);    
        }
    }
}