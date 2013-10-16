package com.androidbook.simplestats;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.android.apps.analytics.Item;
import com.google.android.apps.analytics.Transaction;

public class SimpleStatsActivity extends Activity {

    GoogleAnalyticsTracker mTracker;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start your statistics tracking
        mTracker = GoogleAnalyticsTracker.getInstance();
        mTracker.startNewSession("UA-xxxxx-1", 30, this);

        setContentView(R.layout.main);
        mTracker.trackPageView("/SimpleStats-Main-Screen");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTracker.stopSession();
    }

    public void onClickRedButton(View v) {
        // Red button clicked.
        Toast.makeText(SimpleStatsActivity.this, "Clicked the Red Button",
                Toast.LENGTH_SHORT).show();
        mTracker.trackEvent("Clicks", "Button", "Red", 0);
    }

    public void onClickBlueButton(View v) {
        // Blue button clicked.
        Toast.makeText(SimpleStatsActivity.this, "Clicked the Blue Button",
                Toast.LENGTH_SHORT).show();
        mTracker.trackEvent("Clicks", "Button", "Blue", 0);
    }
    
    public void onClickDie1Button(View v) {
        // Blue button clicked.
        Toast.makeText(SimpleStatsActivity.this, "Clicked the Die1 Button",
                Toast.LENGTH_SHORT).show();
        mTracker.trackEvent("Die", "Hard", "One", 15);
    }
    
    
    public void onClickDie2Button(View v) {
        // Blue button clicked.
        int num = (int) (Math.random()*10)+1000;
        Toast.makeText(SimpleStatsActivity.this, "Clicked the Die2 ("+num+") Button",
                Toast.LENGTH_SHORT).show();
        mTracker.trackEvent("Die", "Easy", "Two", num);
    }

    public void onClickBuyButton(View v)
    {
        // Buy button clicked.
        Toast.makeText(SimpleStatsActivity.this, "Clicked the Buy Button", Toast.LENGTH_SHORT).show();
        
        // Transaction sample
        String orderID = "1001" + new Date().toString();
        
        Transaction.Builder transactionBuilder = new Transaction.Builder( 
                orderID,   
                2.99)    ;


        transactionBuilder.setStoreName("My Game Store");
        transactionBuilder.setShippingCost(0);
        transactionBuilder.setTotalTax(0);
                
        mTracker.addTransaction(transactionBuilder.build());
        
        // Item #1
        Item.Builder itemBuilder = new Item.Builder(
                orderID,
                "SKU_123",
                1.99,
                1);
        
        itemBuilder.setItemCategory("GAME CREDITS");
        itemBuilder.setItemName("1 Game Credit");

        mTracker.addItem(itemBuilder.build());
    
        // Item #2
        Item.Builder itemBuilder2 = new Item.Builder(
                orderID,
                "SKU_345",
                0.99,
                1);
        
        itemBuilder2.setItemCategory("LIFE POINTS");
        itemBuilder2.setItemName("1 Life Point");

        mTracker.addItem(itemBuilder2.build());

        // Commit the transaction and dispatch it
        mTracker.trackTransactions();
    }
}
