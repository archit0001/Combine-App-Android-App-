package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DeezerSavedSongDetailText extends AppCompatActivity {


    private TextView infoView;
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_deezer);

        infoView = (TextView) findViewById(R.id.cityDetailText);
        infoView.setMaxLines(Integer.MAX_VALUE);//Set max.line to textView
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);//Initilize Floating Button

        Intent intent = getIntent();//Get intent

        final Bundle bundle = intent.getExtras();

        infoView.setText("Album : " + bundle.getString("album") + "\nTitle : " + bundle.getString("title") + "\nDuration : " + Double.parseDouble(bundle.getString("duration")) / 60 + " Minute");


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                Intent backToActivity = new Intent();
                backToActivity.putExtra("id", bundle.getLong("id"));//set value for Key id
                backToActivity.putExtra("position", bundle.getInt("position"));
                DeezerSavedSongDetailText.this.setResult(Activity.RESULT_OK, backToActivity);//send data back to onActivityResult()
                DeezerSavedSongDetailText.this.finish();//Go to back Activity
            }
        });
    }
}