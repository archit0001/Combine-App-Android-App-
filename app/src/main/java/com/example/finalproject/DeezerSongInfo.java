package com.example.finalproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DeezerSongInfo extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_info);

        TextView textView = (TextView) findViewById(R.id.infoText);
        String info = "Version junior\n" +
                "Just simple trailer\n\n " +
                "By Archit Anghan";
        textView.setText(info);

    }
}
