package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class GeoSavedCityDetailText extends AppCompatActivity {


    private TextView infoView;
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_geo);

        infoView = (TextView) findViewById(R.id.cityDetailText);
        infoView.setMaxLines(Integer.MAX_VALUE);//Set max.line to textView
        deleteButton = (FloatingActionButton) findViewById(R.id.deleteButton);//Initilize Floating Button

        Intent intent = getIntent();//Get intent

        final Bundle bundle = intent.getExtras();

        infoView.setText("City : " + bundle.getString("city") + "\nCountry : " + bundle.getString("country") + "\nRegion : " + bundle.getString("region") + "\nCurrency : " + bundle.getString("currency") + "\nLatitude : " + bundle.getString("lat") + "\nLongitude : " + bundle.getString("lon"));


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View d) {
                Intent backToActivity = new Intent();
                backToActivity.putExtra("id", bundle.getLong("id"));//set value for Key id
                backToActivity.putExtra("position", bundle.getInt("position"));
                GeoSavedCityDetailText.this.setResult(Activity.RESULT_OK, backToActivity);//send data back to GeoSaveCity in onActivityResult()
                GeoSavedCityDetailText.this.finish();//Go to back Activity
            }
        });
    }
}