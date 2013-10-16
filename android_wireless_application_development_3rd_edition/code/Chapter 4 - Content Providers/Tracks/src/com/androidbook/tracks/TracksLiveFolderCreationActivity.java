package com.androidbook.tracks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.LiveFolders;

import com.androidbook.gpx.TrackPointProvider;

public class TracksLiveFolderCreationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
        final String action = intent.getAction();
        if (LiveFolders.ACTION_CREATE_LIVE_FOLDER.equals(action)) {

            final Intent resultIntent = new Intent();

            resultIntent.setData(TrackPointProvider.LIVE_URI);
            resultIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_NAME, "GPX Sample");
            resultIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_ICON, Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher));
            resultIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_DISPLAY_MODE, LiveFolders.DISPLAY_MODE_LIST);

            setResult(RESULT_OK, resultIntent);
        } 
    
    }

}