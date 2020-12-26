package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class GeoSavedCityList extends AppCompatActivity {

    public static final int SAVED_CITY_TEXT = 21;
    GeoSavedDataPojo geoSavedDataPojo;
    private GeoDatabaseHelper geoDatabaseHelper;
    private ListView listView;
    private List<GeoSavedDataPojo> allList = new ArrayList<>();
    private GeoListAdapter geoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_saved_city);

        listView = findViewById(R.id.savedCityList);
        geoDatabaseHelper = new GeoDatabaseHelper(this);//Intialize Database

        allList = geoDatabaseHelper.getAll();//Get all data from Database

        geoListAdapter = new GeoListAdapter(allList, this);//Set list to Adapter
        listView.setAdapter(geoListAdapter);//Set Adapter

        listView.setOnItemClickListener((parent, view, position, id) -> {

            geoSavedDataPojo = (GeoSavedDataPojo) geoListAdapter.getItem(position);

            Bundle bundle = new Bundle();
            bundle.putLong("id", geoSavedDataPojo.getId());//Data send to the DefiantionText class
            bundle.putInt("position", position);//Data send to the DefiantionText class
            bundle.putString("country", geoSavedDataPojo.getCountry());//Data send to the DefiantionText class
            bundle.putString("region", geoSavedDataPojo.getRegion());
            bundle.putString("city", geoSavedDataPojo.getCity());
            bundle.putString("currency", geoSavedDataPojo.getCurrency());
            bundle.putString("lat", geoSavedDataPojo.getLatitude());
            bundle.putString("lon", geoSavedDataPojo.getLongitude());

            Intent intent = new Intent(this, GeoSavedCityDetailText.class);//Go to next DefiantionText class
            intent.putExtras(bundle);//Send bundle to DefiantionText class
            startActivityForResult(intent, SAVED_CITY_TEXT);//Start intent

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVED_CITY_TEXT) {//get requestCode
            if (resultCode == RESULT_OK) {
                long id = data.getLongExtra("id", 0);//get value for key id
                int position = data.getIntExtra("position", 0);//get value for key position
                deleteCityId(id, position);//Delete ID
            }
        }

    }

    public void deleteCityId(long id, int position) {
        geoDatabaseHelper.deleteId(id);//Delete id from Database
        allList.remove(position);//Remove from list
        geoListAdapter.notifyDataSetChanged();//Update Adapter view

    }


}
