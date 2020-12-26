package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Combine extends AppCompatActivity {

    Button soccer;
    Button lyrcis;
    Button geo;
    Button deezer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.combine);
        soccer = (Button) findViewById(R.id.Soccer);
        lyrcis = (Button) findViewById(R.id.Lyrics);
        geo = (Button) findViewById(R.id.Geodata);
        deezer = (Button) findViewById(R.id.Deezer);

        soccer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View n) {
                Intent intent = new Intent(Combine.this, MainActivity.class);
                startActivity(intent);
            }
        });

        lyrcis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View n) {
                Intent intent = new Intent(Combine.this, LyricsMainActivity.class);
                startActivity(intent);
            }
        });
        geo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View n) {
                Intent intent = new Intent(Combine.this, GeoMainActivity.class);
                startActivity(intent);
            }
        });
        deezer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View n) {
                Intent intent = new Intent(Combine.this, DeezerMainActivity.class);
                startActivity(intent);
            }
        });

    }


}
