package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class GeoDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "GeoProject";
    public final static String TABLE_NAME = "Geo_data";
    public final static String COL_CON = "country";
    public final static String COL_REGION = "region";
    public final static String COL_CITY = "city";
    public final static String COL_CURRENCY = "currency";
    public final static String COL_LATITUDE = "latitude";
    public final static String COL_LONGITUDE = "longitude";
    public static final String COL_ID = "id";
    public final static int VERSION_NUMBER = 1;
    public final static String[] ALL_COLS = new String[]{COL_ID, COL_CON, COL_REGION, COL_CITY, COL_CURRENCY, COL_LATITUDE, COL_LONGITUDE};
    SQLiteDatabase db;

    public GeoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table query
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER primary key autoincrement, " + COL_CON + " text, " + COL_REGION + " text, " + COL_CITY + " text, " + COL_CURRENCY + " text, " + COL_LATITUDE + " text, " + COL_LONGITUDE + " text );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old table
        db.execSQL("drop table if exists " + TABLE_NAME);
        //Create new table
        onCreate(db);
    }

    public long insertValue(String country, String region, String city, String currency, String latitude, String longitude) {

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CON, country);//set country to country column
        cv.put(COL_REGION, region);//set region to region column
        cv.put(COL_CITY, city);
        cv.put(COL_CURRENCY, currency);
        cv.put(COL_LATITUDE, latitude);
        cv.put(COL_LONGITUDE, longitude);
        return db.insert(TABLE_NAME, null, cv);//return id

    }

    public void deleteId(long id) {//Delete Id
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = '" + id + "';");
    }


    public List<GeoSavedDataPojo> getAll() {//Get data from database
        SQLiteDatabase db = getWritableDatabase();
        List<GeoSavedDataPojo> list = new ArrayList<>();

        //get all the results from the databse
        Cursor cursor = db.query(TABLE_NAME, ALL_COLS, null, null, null, null, null);

        //Loop for getting nxt items
        while (cursor.moveToNext()) {

            //Find id,country,region, city, currency, lat, lon in column
            final long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            final String country = cursor.getString(cursor.getColumnIndex(COL_CON));
            final String region = cursor.getString(cursor.getColumnIndex(COL_REGION));
            final String city = cursor.getString(cursor.getColumnIndex(COL_CITY));
            final String currency = cursor.getString(cursor.getColumnIndex(COL_CURRENCY));
            final String lat = cursor.getString(cursor.getColumnIndex(COL_LATITUDE));
            final String lon = cursor.getString(cursor.getColumnIndex(COL_LONGITUDE));
            //add new object to list
            list.add(new GeoSavedDataPojo(id, country, region, city, currency, lat, lon));

        }
        cursor.close();
        return list;//Return list
    }

    public Cursor getData() {
        db = this.getReadableDatabase();

        Cursor c = db.rawQuery("select * from " + TABLE_NAME + ";", null);
        Log.d("Cursor Object", DatabaseUtils.dumpCursorToString(c));

        return c;
    }

}
