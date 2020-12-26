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

public class DeezerDatabaseHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "DeezerSong";
    public final static String TABLE_NAME = "Deezer_song_data";
    public final static String COL_ALBUM = "album";
    public final static String COL_TITLE = "title";
    public final static String COL_DURATION = "duration";
    public static final String COL_ID = "id";
    public final static int VERSION_NUMBER = 1;
    public final static String[] ALL_COLS = new String[]{COL_ID, COL_ALBUM, COL_TITLE, COL_DURATION};
    SQLiteDatabase db;

    public DeezerDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create table query
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER primary key autoincrement, " + COL_ALBUM + " text, " + COL_TITLE + " text, " + COL_DURATION + " text );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old table
        db.execSQL("drop table if exists " + TABLE_NAME);
        //Create new table
        onCreate(db);
    }

    public long insertAlbumTitle(String album, String title, String duration) {

        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ALBUM, album);//set album to album column
        cv.put(COL_TITLE, title);//set title to title coloumn
        cv.put(COL_DURATION, duration);

        return db.insert(TABLE_NAME, null, cv);//return id

    }

    public void deleteId(long id) {//Delete Id
        db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME + " WHERE " + COL_ID + " = '" + id + "';");
    }


    public List<DeezerSavedDataPojo> getAll() {//Get data from database
        SQLiteDatabase db = getWritableDatabase();
        List<DeezerSavedDataPojo> list = new ArrayList<>();

        //get all the results from the databse
        Cursor cursor = db.query(TABLE_NAME, ALL_COLS, null, null, null, null, null);

        //Loop for getting nxt items
        while (cursor.moveToNext()) {

            //Find id,album,Title in column
            final long id = cursor.getLong(cursor.getColumnIndex(COL_ID));
            final String album = cursor.getString(cursor.getColumnIndex(COL_ALBUM));
            final String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
            final String duration = cursor.getString(cursor.getColumnIndex(COL_DURATION));

            //add new object to list
            list.add(new DeezerSavedDataPojo(id, album, title, duration));

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
