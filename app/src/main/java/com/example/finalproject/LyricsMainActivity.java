package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class LyricsMainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    RelativeLayout toolLayout;
    SharedPreferences sp;
    String[] lyricsArray;
    String artistWord = null;
    String titleWord = null;
    String lyrics = null;
    //  private ListView listView;
    //  private TextView tView;
    private EditText artist;
    private EditText title;
    private Button sButton;
    private ListView lView;
    private ProgressBar pBar;
    private ArrayList<LyricsSavedDataPojo> alList = new ArrayList<>();
    private ArrayList<String> lyricsList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private LyricsDatabaseHelper lyricsDatabaseHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_lyrics, menu);
        return true;
    }

    //Menu List
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.about) {
            alertMessage();
            makeText(this, "info", LENGTH_SHORT).show();
//            Intent intent = new Intent(this, SongLyricsInfo.class);
//            startActivity(intent);
//            makeText(this, "Info", LENGTH_LONG).show();
        }

        if (itemId == R.id.addToList) {
            if (artist.getText().toString().equals(artistWord)) {
                addData(artistWord, titleWord, lyrics);//add Lyrics, Lyrics to database
                makeText(this, "Song added in list", LENGTH_SHORT).show();
            }
        }

        if (itemId == R.id.saved) {
            makeText(this, "saved songList", LENGTH_SHORT).show();

            Intent intent = new Intent(this, LyricsSavedSongArtist.class);
            startActivity(intent);
        }

        if (itemId == R.id.exit) {
            showSnackbar(myToolbar);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyrics_activity_main);


        //Get the fields from the screen:
        toolLayout = findViewById(R.id.main);
        artist = (EditText) findViewById(R.id.artistWord);
        title = (EditText) findViewById(R.id.titleWord);
        sButton = (Button) findViewById(R.id.search);
        lView = (ListView) findViewById(R.id.setLyrics);
        pBar = (ProgressBar) findViewById(R.id.progressBarLyrics);
        //  listView=(ListView)findViewById(R.id.savedArtistList);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lyricsList);

        lyricsDatabaseHelper = new LyricsDatabaseHelper(this);


        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                pBar.setVisibility(View.VISIBLE);
                lyricsList.clear();//Clear Lyrics list
                arrayAdapter.notifyDataSetChanged();//Update Adapter

                SharedPreferences.Editor editor = sp.edit();//
                artistWord = artist.getText().toString();
                titleWord = title.getText().toString();
                editor.putString("Artist", artistWord);
                editor.putString("title", titleWord);
                editor.commit();

                String artistText = null;
                String titleText = null;
                try {
                    artistText = URLEncoder.encode(artistWord, "utf-8");
                    titleText = URLEncoder.encode(titleWord, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                lyricsquery query = new lyricsquery();

                query.execute("https://api.lyrics.ovh/v1/" + artistText + "/" + titleText);

            }
        });

        sp = getSharedPreferences("lastInput", Context.MODE_PRIVATE);
        String savedArtist = sp.getString("Artist", "");
        String savedTitle = sp.getString("title", "");
        artist.setText(savedArtist);
        title.setText(savedTitle);

    }

    public void showSnackbar(Toolbar view) {
//Showing snackbar
        final Snackbar sb = Snackbar.make(lView, "Want to exit?", Snackbar.LENGTH_LONG);
        sb.setAction("Exit", e -> finish());
        sb.show();
    }

    public void addData(String artist, String title, String lyrics) {//add data to database methode

        LyricsSavedDataPojo lyricsSavedDataPojo = new LyricsSavedDataPojo(artist, title, lyrics);
        long id = lyricsDatabaseHelper.insertArtistTitle(artist, title, lyrics);
        lyricsSavedDataPojo.setId(id);

        if (!(artist.equals("") & title.equals(""))) {
            alList.add(lyricsSavedDataPojo);
        } else {
            makeText(this, "Please enter a song", LENGTH_SHORT);
        }
    }

    public void alertMessage() {

        View middle = getLayoutInflater().inflate(R.layout.lyrics_activity_info, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.d_help_tittle);
        builder.setMessage(R.string.lyrics_info_text);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();

    }

    private class lyricsquery extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String s) {

            lView.setAdapter(arrayAdapter);
            pBar.setProgress(100);
            pBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pBar.setProgress(values[0]);
        }

        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            String line = null;
            String urlString = strings[0];
            publishProgress(25);

            try {
                URL wordURL = new URL(urlString);
                urlConnection = (HttpURLConnection) wordURL.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                BufferedReader reader = null;
                publishProgress(25);
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"), 8);
                line = reader.readLine();
                publishProgress(50);
                JSONObject jObject = new JSONObject(line);
                lyrics = jObject.getString("lyrics");

                Log.d("size1", String.valueOf(lyricsList.size()));
                lyricsArray = lyrics.split("\r\n|\n\n\n");//add Lyrics to the list
                for (int i = 0; i < lyricsArray.length; i++) {
                    lyricsList.add(lyricsArray[i]);
                }
                publishProgress(75);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Finished";
        }


    }

}