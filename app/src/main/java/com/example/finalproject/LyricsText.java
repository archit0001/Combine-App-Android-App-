package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class LyricsText extends AppCompatActivity {


    LyricsDatabaseHelper lyricsDatabaseHelper;
    LyricsSavedSongArtist lyricsSavedSongArtist;
    private TextView lyricsView;
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyricsdelete);

        lyricsView = (TextView) findViewById(R.id.lyricsText);
        lyricsView.setMaxLines(Integer.MAX_VALUE);//Set max.line to textView
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);//Initilize Floating Button

        Intent intent = getIntent();//Get intent

        final Bundle bundle = intent.getExtras();

        lyricsView.setText(bundle.getString("lyrics"));


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                Intent backToActivity = new Intent();
                backToActivity.putExtra("id", bundle.getLong("id"));//set value for Key id
                backToActivity.putExtra("position", bundle.getInt("position"));
                LyricsText.this.setResult(Activity.RESULT_OK, backToActivity);//send data back to Savedlyrics in onActivityResult()
                LyricsText.this.finish();//Go to back Activity
            }
        });
    }
}