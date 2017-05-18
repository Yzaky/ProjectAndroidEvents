package etien.projectandroidevents;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;

public class FeedReaderDbHelper extends SQLiteOpenHelper {

    static private String TABLE_NAME = "NOSFAVORIS";
    static private String TITLE = "title";
    static private String DESCRIPTION = "description";
    static private String START_TIME = "start_time";
    static private String LONGITUDE = "longitude";
    static private String LATITUDE = "latitude";
    static private String COUNTRYNAME = "country_name";
    static private String REGIONNAME = "region_name";
    static private String CITYNAME = "city_name";
    static private String VENUEADDRESS = "venue_address";
    static private String IMAGEURL = "image_url";

    private final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TITLE + " TEXT," +
                    DESCRIPTION + " TEXT," +
                    START_TIME + " TEXT," +
                    LONGITUDE + " TEXT," +
                    LATITUDE + " TEXT," +
                    COUNTRYNAME + " TEXT," +
                    REGIONNAME + " TEXT," +
                    CITYNAME + " TEXT," +
                    VENUEADDRESS + " TEXT," +
                    IMAGEURL + ")";

    private final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedReader.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void ajoutUnFavoris(Context context, String title, String description, String start_time,
                               String longitude, String latitude,
                               String country_name, String region_name,
                               String city_name, String venue_address,
                               String image_url) {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);

        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(TITLE, title);
        values.put(DESCRIPTION, description);
        values.put(START_TIME, start_time);
        values.put(LONGITUDE, longitude);
        values.put(LATITUDE, latitude);
        values.put(COUNTRYNAME, country_name);
        values.put(REGIONNAME, region_name);
        values.put(CITYNAME, city_name);
        values.put(VENUEADDRESS, venue_address);
        values.put(IMAGEURL, image_url);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_NAME, null, values);
    }

    public Object[] readFavoris(Context context) {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                TITLE, START_TIME, DESCRIPTION, LONGITUDE, LATITUDE, IMAGEURL,
                COUNTRYNAME, REGIONNAME, CITYNAME, VENUEADDRESS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "";
        Cursor cursor = db.query(
                TABLE_NAME,                               // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        int i = 0;

        int nbRows = cursor.getCount();
        String[] titles = new String[nbRows];
        String[] starttimes = new String[nbRows];
        String[] descriptions = new String[nbRows];
        String[] longitudes = new String[nbRows];
        String[] latitudes = new String[nbRows];
        String[] images = new String[nbRows];
        String[] country_names = new String[nbRows];
        String[] region_names = new String[nbRows];
        String[] city_names = new String[nbRows];
        String[] venue_address = new String[nbRows];

        while (cursor.moveToNext()) {

            titles[i] = cursor.getString(cursor.getColumnIndex(TITLE));
            starttimes[i] = cursor.getString(cursor.getColumnIndex(START_TIME));
            descriptions[i] = cursor.getString(cursor.getColumnIndex(DESCRIPTION));

            longitudes[i] = cursor.getString(cursor.getColumnIndex(LONGITUDE));
            latitudes[i] = cursor.getString(cursor.getColumnIndex(LATITUDE));

            Log.d("DB", longitudes[i]);

            images[i] = cursor.getString(cursor.getColumnIndex(IMAGEURL));
            country_names[i] = cursor.getString(cursor.getColumnIndex(COUNTRYNAME));
            region_names[i] = cursor.getString(cursor.getColumnIndex(REGIONNAME));
            city_names[i] = cursor.getString(cursor.getColumnIndex(CITYNAME));
            venue_address[i] = cursor.getString(cursor.getColumnIndex(VENUEADDRESS));
            i++;
        }
        cursor.close();

        return new Object[]{nbRows, titles, starttimes, descriptions, longitudes, latitudes,
                images, country_names, region_names, city_names, venue_address
        };

    }

    public void deleteUnFavoris(Context context, String title) {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define 'where' part of query.
        String selection = TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {title};
        // Issue SQL statement.
        db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public boolean isBookmarked(Context context, String title) {

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {TITLE};

        // Filter results WHERE "title" = 'My Title'
        String selection = TITLE + "=?";
        String[] selectionArgs = {title};

        // How you want the results sorted in the resulting Cursor
        String sortOrder = "";
        Cursor cursor = db.query(
                TABLE_NAME,                               // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        int nbRows = cursor.getCount();
        cursor.close();

        return nbRows != 0;
    }
}