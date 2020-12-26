package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DeezerSavedSongsList extends AppCompatActivity {

    public static final int SAVED_SONG_TEXT = 21;
    DeezerSavedDataPojo deezerSavedDataPojo;
    private DeezerDatabaseHelper deezerDatabaseHelper;
    private ListView listView;
    private List<DeezerSavedDataPojo> allList = new ArrayList<>();
    private DeezerListAdapter deezerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_saved_songs);

        listView = findViewById(R.id.savedSongsList);
        deezerDatabaseHelper = new DeezerDatabaseHelper(this);//Intialize Database

        allList = deezerDatabaseHelper.getAll();//Get all data from Database

        deezerListAdapter = new DeezerListAdapter(allList, this);//Set list to Adapter
        listView.setAdapter(deezerListAdapter);//Set Adapter

        listView.setOnItemClickListener((parent, view, position, id) -> {

            deezerSavedDataPojo = (DeezerSavedDataPojo) deezerListAdapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putInt("position", position);//Data send to the Detail class
            bundle.putString("album", deezerSavedDataPojo.getAlbum());//Data send to the Detail class
            bundle.putString("title", deezerSavedDataPojo.getTitle());
            bundle.putString("duration", deezerSavedDataPojo.getDuration());

            Intent intent = new Intent(this, DeezerSavedSongDetailText.class);//Go to next Detail class
            intent.putExtras(bundle);//Send bundle to Detail class
            startActivityForResult(intent, SAVED_SONG_TEXT);//Start intent

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVED_SONG_TEXT) {//get requestCode from Detail class
            if (resultCode == RESULT_OK) {
                long id = data.getLongExtra("id", 0);//get value for key id
                int position = data.getIntExtra("position", 0);//get value for key position
                deleteSongId(id, position);//Delete song
            }
        }

    }

    public void deleteSongId(long id, int position) {
        deezerDatabaseHelper.deleteId(id);//Delete id from Database
        allList.remove(position);//Remove from list
        deezerListAdapter.notifyDataSetChanged();//Update Adapter view

    }


}
