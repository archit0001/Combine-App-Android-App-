package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GeoCityDetail extends AppCompatActivity {
    FrameLayout frameLayout;
    private TextView cityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_city_info_);

        frameLayout = (FrameLayout) findViewById(R.id.mapFrame);
        cityView = (TextView) findViewById(R.id.cityInfo);
        cityView.setMaxLines(Integer.MAX_VALUE);//Set max.line to textView
        Intent intent = getIntent();//Get intent

        final Bundle bundle = intent.getExtras();

        cityView.setText("City : " + bundle.getString("city") + "\nCountry : " + bundle.getString("country") + "\nRegion : " + bundle.getString("region") + "\nCurrency : " + bundle.getString("currency") + "\nLatitude : " + bundle.getString("lat") + "\nLongitude : " + bundle.getString("lon"));

        GeoMapsFragment mFragment = new GeoMapsFragment(); //add a DetailFragment
        mFragment.setArguments(bundle); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mapFrame, mFragment) //Add the fragment in FrameLayout
                .addToBackStack("AnyName") //make the back button undo the transaction
                .commit(); //actually load the fragment.
    }

}
