package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class LyricsSavedSongArtist extends AppCompatActivity {

    public static final int LYRICS_TEXT = 21;
    LyricsSavedDataPojo lyricsSavedDataPojo;
    private LyricsDatabaseHelper lyricsDatabaseHelper;
    private ListView listView;
    private List<LyricsSavedDataPojo> allList = new ArrayList<>();
    private LyricsListAdapter lyricsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lyrics);

        listView = findViewById(R.id.savedArtistList);
        lyricsDatabaseHelper = new LyricsDatabaseHelper(this);//Intialize Database

        allList = lyricsDatabaseHelper.getAll();//Get all data Lyrics from Database

        lyricsListAdapter = new LyricsListAdapter(allList, this);//Set list to Adapter
        listView.setAdapter(lyricsListAdapter);//Set Adapter

        listView.setOnItemClickListener((parent, view, position, id) -> {

            lyricsSavedDataPojo = (LyricsSavedDataPojo) lyricsListAdapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putLong("id", lyricsSavedDataPojo.getId());//Data send to the LyricsText class
            bundle.putInt("position", position);//Data send to the LyricsText class
            bundle.putString("lyrics", lyricsSavedDataPojo.getLyrics());//Data send to the LyricsText class

            Intent intent = new Intent(this, LyricsText.class);//Go to next LyricsText class
            intent.putExtras(bundle);//Send bundle to LyricsText class
            startActivityForResult(intent, LYRICS_TEXT);//Start intent

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LYRICS_TEXT) {//get requestCode from LyricsText class
            if (resultCode == RESULT_OK) {
                long id = data.getLongExtra("id", 0);//get value for key id
                int position = data.getIntExtra("position", 0);//get value for key position
                deleteWordId(id, position);//Delete Id
            }
        }

    }

    public void deleteWordId(long id, int position) {
        lyricsDatabaseHelper.deleteId(id);//Delete id from Database
        allList.remove(position);//Remove from list
        lyricsListAdapter.notifyDataSetChanged();//Update Adapter view

    }


}
