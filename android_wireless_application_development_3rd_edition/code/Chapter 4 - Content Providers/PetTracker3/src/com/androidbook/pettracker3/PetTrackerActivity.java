package com.androidbook.pettracker3;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class PetTrackerActivity extends Activity {

	protected PetTrackerDatabaseHelper mDatabase = null; 
	protected SQLiteDatabase mDB = null;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDatabase = new PetTrackerDatabaseHelper(this.getApplicationContext());
		mDB = mDatabase.getWritableDatabase();
		
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if(mDB != null)
		{
			mDB.close();
		}
		
		if(mDatabase != null)
		{
			mDatabase.close();
		}
	}
}