package com.example.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_PATH_SUFFIX = "/databases/";
    private static String DB_NAME = "soccer.db";
    private final Context context;
    private SQLiteDatabase db;
    private String DB_PATH;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 3);
        this.context = context;
        DB_PATH = context.getDatabasePath("soccer.db").getPath();
        //getFilesDir().getPath()+ "/data/data/" + context.getPackageName() + "/" + "databases/";
    }

    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        if (!dbExist) {
            getWritableDatabase();
            copyDataBase();
        } else {
            this.getWritableDatabase();
        }
    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH);
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public Cursor getData(String Query) {
        String myPath = DB_PATH;
        Cursor c = null;
        try {
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            c = db.rawQuery(Query, null);
        } catch (Exception e) {
            Log.e("Err", e.toString());
        }
        return c;
    }

    public void dml(String Query) {
        String myPath = DB_PATH;
        if (db == null)
            db = openDataBase();
        try {
            db.execSQL(Query);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    public void CopyDataBaseFromAsset() throws IOException {
//        Context context = ctx;
        String str = DB_NAME;
        String str2 = "Database";
        if (context.getDatabasePath(str).exists()) {
            Log.e(str2, "db already exist");
            return;
        }
        Log.e(str2, "New database has been copied to device!");
        InputStream open = context.getAssets().open(str);
//        String databasePath = getDatabasePath();
        StringBuilder sb = new StringBuilder();
        sb.append(context.getApplicationInfo().dataDir);
        sb.append(DB_PATH_SUFFIX);
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdir();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(DB_PATH);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = open.read(bArr);
            if (read > 0) {
                fileOutputStream.write(bArr, 0, read);
            } else {
                fileOutputStream.flush();
                fileOutputStream.close();
                open.close();
                Log.e(str2, "New database has been copied to device! success");
                return;
            }
        }
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        String str = "copy database";
        File databasePath = context.getDatabasePath(DB_NAME);
        if (!databasePath.exists()) {
            try {
                CopyDataBaseFromAsset();
//                System.out.println("Copying sucess from Assets folder");
                Log.e(str, "sucess");
            } catch (IOException e) {
                String str2 = "Error creating source database";
                Log.e(str, str2);
                throw new RuntimeException(str2, e);
            }
        }
        return SQLiteDatabase.openDatabase(databasePath.getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public ArrayList<Match> getMatches(String table, String type) {
        ArrayList<Match> arrayList = new ArrayList<>();

        String query = "";
        switch (type) {
            case "":
                query = "select * from '" + table + "'";
                break;
            case "views":
                query = "select * from '" + table + "' order by views + 0 desc";
                break;
            case "rate":
                query = "select * from '" + table + "' order by avg_rate + 0 desc";
                break;
        }


        Cursor cursor = getData(query);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                Match itemvideo = new Match();

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                itemvideo.setId(id);

                String title = cursor.getString(cursor.getColumnIndex("title"));
                itemvideo.setTitle(title);

                String url = cursor.getString(cursor.getColumnIndex("url"));
                itemvideo.setUrl(url);

                String thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"));
                itemvideo.setThumbnail(thumbnail);

                String date = cursor.getString(cursor.getColumnIndex("date"));
                itemvideo.setDate(date);

                Side ss = new Side();
                String side1 = cursor.getString(cursor.getColumnIndex("side1"));
                String s1_url = cursor.getString(cursor.getColumnIndex("s1_url"));
                ss.setName(side1);
                ss.setUrl(s1_url);
                itemvideo.setSide1(ss);


                Side ss2 = new Side();
                String side2 = cursor.getString(cursor.getColumnIndex("side2"));
                String s2_url = cursor.getString(cursor.getColumnIndex("s2_url"));
                ss2.setName(side2);
                ss2.setUrl(s2_url);
                itemvideo.setSide2(ss2);

                Competition cc = new Competition();
                String cmp = cursor.getString(cursor.getColumnIndex("competition"));
                cc.setName(cmp);

                String highlight = cursor.getString(cursor.getColumnIndex("highlight"));
                itemvideo.setHls_url(highlight);

                arrayList.add(itemvideo);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return arrayList;
    }


    public ArrayList<Match> getFavMatches(String table, String type) {
        ArrayList<Match> arrayList = new ArrayList<>();

        String query = "";
        switch (type) {
            case "":
                query = "select * from '" + table + "'";
                break;
            case "views":
                query = "select * from '" + table + "' order by views + 0 desc";
                break;
            case "rate":
                query = "select * from '" + table + "' order by avg_rate + 0 desc";
                break;
        }


        Cursor cursor = getData(query);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                Match itemvideo = new Match();

                int id = cursor.getInt(cursor.getColumnIndex("id"));
                itemvideo.setId(id);

                String title = cursor.getString(cursor.getColumnIndex("title"));
                itemvideo.setTitle(title);

                String url = cursor.getString(cursor.getColumnIndex("url"));
                itemvideo.setUrl(url);

                String thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"));
                itemvideo.setThumbnail(thumbnail);

                String date = cursor.getString(cursor.getColumnIndex("date"));
                itemvideo.setDate(date);

                Side ss = new Side();
                String side1 = cursor.getString(cursor.getColumnIndex("side1"));
                String s1_url = cursor.getString(cursor.getColumnIndex("s1_url"));
                ss.setName(side1);
                ss.setUrl(s1_url);
                itemvideo.setSide1(ss);


                Side ss2 = new Side();
                String side2 = cursor.getString(cursor.getColumnIndex("side2"));
                String s2_url = cursor.getString(cursor.getColumnIndex("s2_url"));
                ss2.setName(side2);
                ss2.setUrl(s2_url);
                itemvideo.setSide2(ss2);

                Competition cc = new Competition();
                String cmp = cursor.getString(cursor.getColumnIndex("competition"));
                cc.setName(cmp);

                String highlight = cursor.getString(cursor.getColumnIndex("highlight"));
                itemvideo.setHls_url(highlight);

                arrayList.add(itemvideo);

                cursor.moveToNext();
            }
            cursor.close();
        }

        return arrayList;
    }


    public Boolean isFav(String id) {
        String selectQuery = "SELECT  * FROM fav WHERE id=" + "'" + id + "'";

        Cursor cursor = getData(selectQuery);
        return cursor != null && cursor.getCount() > 0;
    }


    public void addtoFavorite(Match itemWallpaper) {
        dml("insert into fav (id,title,url,thumbnail,date,side1,s1_url,side2,s2_url,competition,highlight) values ('"
                + itemWallpaper.getId() + "','" + itemWallpaper.getTitle() + "','" + itemWallpaper.getUrl() + "','" +
                itemWallpaper.getThumbnail() + "','" + itemWallpaper.getDate() + "','" +
                itemWallpaper.getSide1().getName() + "','" + itemWallpaper.getSide1().getUrl() + "','" +
                itemWallpaper.getSide2().getName() + "','" + itemWallpaper.getSide2().getUrl() + "','" +
                itemWallpaper.getCompetition().getName() + "','" + itemWallpaper.getHls_url() + "')");
    }

    public void removeFav(String id) {
        dml("delete from fav where id = '" + id + "'");
    }

    //rate == share
    //avg_rate == comments
    public void addMatch(Match itemWallpaper, String table) {
        dml("insert into '" + table + "' (id,title,url,thumbnail,date,side1,s1_url,side2,s2_url,competition,highlight)" +
                " values ('" + itemWallpaper.getId() + "','" + itemWallpaper.getTitle() + "','" + itemWallpaper.getUrl() + "','" +
                itemWallpaper.getThumbnail() + "','" + itemWallpaper.getDate() + "','" +
                itemWallpaper.getSide1().getName() + "','" + itemWallpaper.getSide1().getUrl() + "','" +
                itemWallpaper.getSide2().getName() + "','" + itemWallpaper.getSide2().getUrl() + "','" +
                itemWallpaper.getCompetition().getName() + "','" + itemWallpaper.getHls_url() + "')");
    }

    public void removeMatch(String table) {
        dml("delete from '" + table + "'");
    }

    public void removeWallByCat(String table, String id) {
        dml("delete from '" + table + "' where cid= '" + id + "'");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("aaa", "upgrade");
        Log.e("aaa -oldVersion", "" + oldVersion);
        Log.e("aaa -newVersion", "" + newVersion);
        try {
            if (db == null) {
                String myPath = DB_PATH;
                db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
            switch (oldVersion) {
                case 1:

                case 2:
                    db.execSQL("ALTER TABLE gif ADD 'total_rate' TEXT");
                    db.execSQL("ALTER TABLE gif ADD 'avg_rate' TEXT");
                    db.execSQL("ALTER TABLE gif ADD 'total_download' TEXT");
                    db.execSQL("ALTER TABLE gif ADD 'tags' TEXT");

                    db.execSQL("ALTER TABLE latest ADD 'total_rate' TEXT");
                    db.execSQL("ALTER TABLE latest ADD 'avg_rate' TEXT");
                    db.execSQL("ALTER TABLE latest ADD 'total_download' TEXT");
                    db.execSQL("ALTER TABLE latest ADD 'tags' TEXT");

                    db.execSQL("ALTER TABLE fav ADD 'total_rate' TEXT");
                    db.execSQL("ALTER TABLE fav ADD 'avg_rate' TEXT");
                    db.execSQL("ALTER TABLE fav ADD 'total_download' TEXT");
                    db.execSQL("ALTER TABLE fav ADD 'tags' TEXT");

                    db.execSQL("ALTER TABLE catlist ADD 'total_rate' TEXT");
                    db.execSQL("ALTER TABLE catlist ADD 'avg_rate' TEXT");
                    db.execSQL("ALTER TABLE catlist ADD 'total_download' TEXT");
                    db.execSQL("ALTER TABLE catlist ADD 'tags' TEXT");

                    db.execSQL("ALTER TABLE about ADD 'ad_pub' TEXT");
                    db.execSQL("ALTER TABLE about ADD 'ad_banner' TEXT");
                    db.execSQL("ALTER TABLE about ADD 'ad_inter' TEXT");
                    db.execSQL("ALTER TABLE about ADD 'isbanner' TEXT");
                    db.execSQL("ALTER TABLE about ADD 'isinter' TEXT");
                    db.execSQL("ALTER TABLE about ADD 'click' TEXT");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}