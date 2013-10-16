package com.androidbook.pettracker3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidbook.pettracker3.PetTrackerDatabase.PetType;
import com.androidbook.pettracker3.PetTrackerDatabase.Pets;

public class PetTrackerListActivity extends PetTrackerActivity {

	protected Cursor mCursor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.showpets);

		// Fill ListView from database
		fillPetList();

		// Handle Go enter more pets button
		final Button gotoEntry = (Button) findViewById(R.id.ButtonEnterMorePets);
		gotoEntry.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				// We're done here. Finish and return to the entry screen
				finish();
			}
		});
	}

	public void fillPetList() {
	
		// SQL Query
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(Pets.PETS_TABLE_NAME + ", "
				+ PetType.PETTYPE_TABLE_NAME);
		queryBuilder.appendWhere(Pets.PETS_TABLE_NAME + "."
				+ Pets.PET_TYPE_ID + "=" + PetType.PETTYPE_TABLE_NAME + "."
				+ PetType._ID);

		// Get the database and run the query
		String asColumnsToReturn[] = {
				Pets.PETS_TABLE_NAME + "." + Pets.PET_NAME,
				Pets.PETS_TABLE_NAME + "." + Pets.PET_IMAGE_URI,
				Pets.PETS_TABLE_NAME + "." + Pets._ID,
				Pets.PETS_TABLE_NAME + "." + Pets.PET_IMAGE_ID,
				PetType.PETTYPE_TABLE_NAME + "." + PetType.PET_TYPE_NAME };
		
		mCursor = queryBuilder.query(mDB, asColumnsToReturn, null, null,
				null, null, Pets.DEFAULT_SORT_ORDER);
		startManagingCursor(mCursor);
		
		// Use an adapter to bind the data to a ListView, where each item is
		// shown as a pet_item layout
		PetListAdapter adapter = new PetListAdapter(this, mCursor, false);	
		ListView av = (ListView) findViewById(R.id.petList);
		av.setAdapter(adapter);

		// Listen for clicks on our ListView
		av.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				final long deletePetId = id;

				// Use an Alert dialog to confirm delete operation
				new AlertDialog.Builder(PetTrackerListActivity.this).setMessage(
						"Delete Pet Record?").setPositiveButton("Delete",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
									int which) {

								// Delete that pet
								deletePet(deletePetId);
								// refresh our listview with a cursor requery
								refillPetList(); 
							}
						}).show();
			}
		});
	}
	
	// We refresh our ListAdapter as necessary
	public void refillPetList() {
		
		mCursor.requery();
		PetListAdapter adapter = new PetListAdapter(this, mCursor, false);
		ListView av = (ListView) findViewById(R.id.petList);
		av.setAdapter(adapter);

	}

	// Delete a pet by id
	public void deletePet(Long id) {
		String astrArgs[] = { id.toString() };
		mDB.delete(Pets.PETS_TABLE_NAME, Pets._ID + "=?", astrArgs);
	}
	
	
	public class PetListAdapter extends CursorAdapter {
	    
	    private final int colIndexPetType;
	    private final int colIndexPetName;
	    private final int colIndexPetImageUri;
	    private final LayoutInflater inflater;

        public PetListAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, autoRequery);
            colIndexPetType = c.getColumnIndex(PetType.PET_TYPE_NAME);
            colIndexPetName = c.getColumnIndex(Pets.PET_NAME);
            colIndexPetImageUri = c.getColumnIndex(Pets.PET_IMAGE_URI);
            
            inflater = (LayoutInflater)context.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // existing view, so simply update petname, pettype, and peturi
            
            // cast type
            RelativeLayout petItem = (RelativeLayout) view;
            PetListItemContainer petItemHolder = (PetListItemContainer)petItem.getTag();
            setPetItem(cursor, petItemHolder);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            // no existing view to reuse, so we'll create a new one
            RelativeLayout petItem = (RelativeLayout) inflater.inflate(R.layout.pet_item, parent, false);
            PetListItemContainer petViewHolder = new PetListItemContainer();
            petViewHolder = new PetListItemContainer();
            petViewHolder.petNameView = (TextView) petItem.findViewById(R.id.TextView_PetName);
            petViewHolder.petTypeView = (TextView) petItem.findViewById(R.id.TextView_PetType);
            petViewHolder.petImageView = (ImageView) petItem.findViewById(R.id.ImageView_PetPic);
            petItem.setTag(petViewHolder);
            
            setPetItem(cursor, petViewHolder);
            
            return petItem;
        }

        private void setPetItem(Cursor cursor, PetListItemContainer petItemHolder) {
            String petType = cursor.getString(colIndexPetType);
            String petName = cursor.getString(colIndexPetName);
            String petImageUri = cursor.getString(colIndexPetImageUri);
            
            petItemHolder.petTypeView.setText(petType);
            petItemHolder.petNameView.setText(petName);
            // TODO: this should be done on a background thread, setImageURI blocks
            petItemHolder.petImageView.setImageURI(Uri.parse(petImageUri));
        }

        // Using this saves the expensive findViewById lookup on each item
        private class PetListItemContainer  {
            TextView petNameView; 
            TextView petTypeView; 
            ImageView petImageView; 
        }
	    
	}
	

	
}
