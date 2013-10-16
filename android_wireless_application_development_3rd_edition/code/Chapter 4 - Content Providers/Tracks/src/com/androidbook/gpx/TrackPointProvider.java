package com.androidbook.gpx;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.LiveFolders;
import android.text.TextUtils;
import android.util.Log;

/**
 * @author Shane
 * */
public class TrackPointProvider extends ContentProvider {
    public static final String _ID = "_id";
    public static final String TIMESTAMP = "timestamp";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String ELEVATION = "elevation";
    public static final String AUTHORITY = "com.androidbook.gpx.TrackPointProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"
            + TrackPointDatabase.TRACKPOINTS_TABLE);
    public static final Uri LIVE_URI = Uri.parse("content://" + AUTHORITY + "/" + TrackPointDatabase.TRACKPOINTS_TABLE
            + "/live");
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/track-points";
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/track-points";
    private static final int TRACKPOINTS = 1;
    private static final int TRACKPOINT_ID = 10;
    private static final int TRACKPOINTS_LIVE = 20;
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, "points", TRACKPOINTS);
        sURIMatcher.addURI(AUTHORITY, "points/#", TRACKPOINT_ID);
        sURIMatcher.addURI(AUTHORITY, "points/live", TRACKPOINTS_LIVE);
    }
    private TrackPointDatabase mDB;

    private static class TrackPointDatabase extends SQLiteOpenHelper {
        private static final String TRACKPOINTS_TABLE = "points";
        private static final String TRACKPOINT_DB_NAME = "trackpoint_data";
        private static final int DATABASE_VERSION = 2;
        private static final String TPDB_SCHEMA = "create table " + TRACKPOINTS_TABLE
                + " (_id integer primary key autoincrement, " + TIMESTAMP + " text not null, " + LATITUDE
                + " integer not null, " + LONGITUDE + " integer not null, " + ELEVATION + " real " + ");";

        TrackPointDatabase(Context context) {
            super(context, TRACKPOINT_DB_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TPDB_SCHEMA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("TPDB", "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TRACKPOINTS_TABLE);
            onCreate(db);
        }
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#delete(android.net.Uri,
     * java.lang.String, java.lang.String[])
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsAffected = 0;
        switch (match) {
        case TRACKPOINTS:
            rowsAffected = sqlDB.delete(TrackPointDatabase.TRACKPOINTS_TABLE, selection, selectionArgs);
            break;
        case TRACKPOINT_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsAffected = sqlDB.delete(TrackPointDatabase.TRACKPOINTS_TABLE, _ID + "=" + id, null);
            } else {
                rowsAffected = sqlDB.delete(TrackPointDatabase.TRACKPOINTS_TABLE, selection + " and " + _ID + "=" + id,
                        selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#getType(android.net.Uri)
     */
    @Override
    public String getType(Uri uri) {
        int matchType = sURIMatcher.match(uri);
        switch (matchType) {
        case TRACKPOINTS:
            return CONTENT_TYPE;
        case TRACKPOINT_ID:
            return CONTENT_ITEM_TYPE;
        default:
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#insert(android.net.Uri,
     * android.content.ContentValues)
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = sURIMatcher.match(uri);
        if (match != TRACKPOINTS) {
            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
            // return null; // or throw an exception
        }
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        long newID = sqlDB.insert(TrackPointDatabase.TRACKPOINTS_TABLE, null, values);
        if (newID > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, newID);
            getContext().getContentResolver().notifyChange(uri, null);
            return newUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#onCreate()
     */
    @Override
    public boolean onCreate() {
        mDB = new TrackPointDatabase(getContext());
        return true;
    }

    private static final HashMap<String, String> TRACKPOINTS_LIVE_FOLDER_PROJECTION_MAP;
    static {
        TRACKPOINTS_LIVE_FOLDER_PROJECTION_MAP = new HashMap<String, String>();
        TRACKPOINTS_LIVE_FOLDER_PROJECTION_MAP.put(LiveFolders._ID, _ID + " as " + LiveFolders._ID);
        TRACKPOINTS_LIVE_FOLDER_PROJECTION_MAP.put(LiveFolders.NAME, TIMESTAMP + " as " + LiveFolders.NAME);
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#query(android.net.Uri,
     * java.lang.String[], java.lang.String, java.lang.String[],
     * java.lang.String)
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // SQLiteQueryBuilder is a helper class that creates the
        // proper SQL syntax for us.
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();
        // Set the table we're querying.
        qBuilder.setTables(TrackPointDatabase.TRACKPOINTS_TABLE);
        // If the query ends in a specific record number, we're
        // being asked for a specific record, so set the
        // WHERE clause in our query.
        switch (sURIMatcher.match(uri)) {
        case TRACKPOINT_ID:
            qBuilder.appendWhere("_id=" + uri.getLastPathSegment());
            break;
        case TRACKPOINTS_LIVE:
            qBuilder.setProjectionMap(TRACKPOINTS_LIVE_FOLDER_PROJECTION_MAP);
            break;
        default:
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        // Make the query.
        Cursor c = qBuilder.query(mDB.getReadableDatabase(), projection, selection, selectionArgs, null, null,
                sortOrder, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /*
     * (non-Javadoc)
     * @see android.content.ContentProvider#update(android.net.Uri,
     * android.content.ContentValues, java.lang.String, java.lang.String[])
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int match = sURIMatcher.match(uri);
        int rowsAffected;
        switch (match) {
        case TRACKPOINTS:
            rowsAffected = sqlDB.update(TrackPointDatabase.TRACKPOINTS_TABLE, values, selection, selectionArgs);
            break;
        case TRACKPOINT_ID:
            String id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsAffected = sqlDB.update(TrackPointDatabase.TRACKPOINTS_TABLE, values, _ID + "=" + id, null);
            } else {
                rowsAffected = sqlDB.update(TrackPointDatabase.TRACKPOINTS_TABLE, values, selection + " and " + _ID
                        + "=" + id, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}
