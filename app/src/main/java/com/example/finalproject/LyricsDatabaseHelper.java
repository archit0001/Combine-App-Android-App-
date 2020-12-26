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

public class LyricsDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "FinalProject";
    public final static String TABLE_NAME = "Song_Lyrics";
    public final static String COL_ARTIST = "artist";
    public final static String COL_TITLE = "title";
    public final static String COL_LYRICS = "lyrics";
    public static final String COL_ID = "id";
    public final static int VERSION_NUMBER = 1;
    public final static String[] ALL_COLS = new String[]{COL_ID, COL_ARTIST, COL_TITLE, COL_LYRICS};
    SQLiteDatabase db;

    public LyricsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table query
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER primary key autoincrement, " + COL_ARTIST + " text, " + COL_TITLE + " text, " + COL_LYRICS + " text );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old table
        db.execSQL("drop table if exists " + TABLE_NAME);
        //Create new table
        onCreate(db);
    }

    //method for storing data
    public long insertArtistTitle(String artist, String title, String lyrics) {

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ARTIST, artist);//set artist to artist column
        cv.put(COL_TITLE, title);//set title to title column
        cv.put(COL_LYRICS, lyrics);
        return db.insert(TABLE_NAME, null, cv);//return id

    }

    //method for delete the data from the table
    public void deleteId(long id) {//Delete ID
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = '" + id + "';");
    }


    public List<LyricsSavedDataPojo> getAll() {//Get data from database
        SQLiteDatabase db = getWritableDatabase();
        List<LyricsSavedDataPojo> list = new ArrayList<>();

        //get all the results from the databse
        Cursor cursor = db.query(TABLE_NAME, ALL_COLS, null, null, null, null, null);

        //Loop for getting nxt items
        while (cursor.moveToNext()) {

            //Find id,artist,lyrics in column
            final long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            final String artist = cursor.getString(cursor.getColumnIndex(COL_ARTIST));
            final String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            final String lyrics = cursor.getString(cursor.getColumnIndex(COL_LYRICS));

            //add new object to list
            list.add(new LyricsSavedDataPojo(id, artist, title, lyrics));

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
