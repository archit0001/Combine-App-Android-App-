package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class DeezerSongDetail extends AppCompatActivity {
    FloatingActionButton addButton;
    private TextView songInfo;
    private ImageView imageView;
    private ArrayList<DeezerSavedDataPojo> savedSongList = new ArrayList<>();
    private DeezerDatabaseHelper deezerDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_info_deezer);
        imageView = (ImageView) findViewById(R.id.imageView);
        songInfo = (TextView) findViewById(R.id.songInfo);
        addButton = (FloatingActionButton) findViewById(R.id.addButton);

        Intent intent = getIntent();//Get intent
        final Bundle bundle = intent.getExtras();
        int position = bundle.getInt("position");

        imageView.setImageBitmap(bundle.getParcelable("image"));
        songInfo.setText("Album : " + bundle.getString("album") + "\nSong Title : " + bundle.getString("title") + "\nDuration : " + String.format("%.2f", (Double.parseDouble(bundle.getString("duration")) / 60)) + " Minute");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(bundle.getString("album"), bundle.getString("title"), bundle.getString("duration"));
                makeText(v.getContext(), "Song added in favourite list", LENGTH_SHORT).show();
            }
        });
    }

    public void addData(String album, String title, String duration) {//add data to database methode
        deezerDatabaseHelper = new DeezerDatabaseHelper(this);
        DeezerSavedDataPojo deezerSavedDataPojo = new DeezerSavedDataPojo(album, title, duration);
        long id = deezerDatabaseHelper.insertAlbumTitle(album, title, duration);
        deezerSavedDataPojo.setId(id);

        savedSongList.add(deezerSavedDataPojo);


    }


}
