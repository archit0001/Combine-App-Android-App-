package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class GeoMainActivity extends AppCompatActivity {

    public static final int CITY_TEXT = 21;
    Toolbar myToolbar;
    RelativeLayout toolLayout;
    SharedPreferences sp;
    String latNum = null;
    String lonNum = null;
    String cityJson, countryJson, regionJson, currencyJson, latJson, lonJson = null;
    private EditText latitude;
    private EditText longitude;
    private Button sButton;
    private ListView lView;
    private ProgressBar pBar;
    private CityListAdaptor cityListAdaptor;
    private ArrayList<GeoSavedDataPojo> savedCityList = new ArrayList<>();
    private ArrayList<GeoSavedDataPojo> cityDetailList = new ArrayList<>();
    private List<String> cityList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private GeoDatabaseHelper geoDatabaseHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_geo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.about) {
            alertMessage();
            makeText(this, "info", LENGTH_SHORT).show();
//            Intent intent = new Intent(this, GeoDataInfo.class);
//            startActivity(intent);
//            makeText(this, "Info", LENGTH_LONG).show();
        }

        if (itemId == R.id.saved) {
            makeText(this, "Saved city list", LENGTH_SHORT).show();

            Intent intent = new Intent(this, GeoSavedCityList.class);
            startActivity(intent);
        }

        if (itemId == R.id.exit) {
            showSnackbar(myToolbar);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_main);


        //Get the fields from the screen:
        toolLayout = findViewById(R.id.main);
        latitude = (EditText) findViewById(R.id.latitude);
        longitude = (EditText) findViewById(R.id.longitude);
        sButton = (Button) findViewById(R.id.search);
        lView = (ListView) findViewById(R.id.setCity);
        pBar = (ProgressBar) findViewById(R.id.progressBarLyrics);
        cityListAdaptor = new CityListAdaptor(cityDetailList, this);
        geoDatabaseHelper = new GeoDatabaseHelper(this);


        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                pBar.setVisibility(View.VISIBLE);
                cityDetailList.clear();//Clear list
                cityListAdaptor.notifyDataSetChanged();//Update Adapter

                SharedPreferences.Editor editor = sp.edit();//
                latNum = latitude.getText().toString();
                lonNum = longitude.getText().toString();
                editor.putString("latitude", latNum);
                editor.putString("longitude", lonNum);
                editor.commit();

                String lat = null;
                String lon = null;
                try {
                    lat = URLEncoder.encode(latNum, "utf-8");
                    lon = URLEncoder.encode(lonNum, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                geoQuery query = new geoQuery();
//API
                query.execute("https://api.geodatasource.com/cities?key=H8X2XAWOVXCGNJE7SIYYC2HHWLLDI71U&lat=" + lat + "&lng=" + lon + "&format=json");

            }

        });

        sp = getSharedPreferences("lastWord", Context.MODE_PRIVATE);
        String savedLat = sp.getString("latitude", "");
        String savedLon = sp.getString("longitude", "");
        latitude.setText(savedLat);
        longitude.setText(savedLon);
//Listview
        lView.setOnItemClickListener((parent, view, position, id) -> {
            Log.d("Boolean", String.valueOf(lView.isClickable()));
            Bundle bundle = new Bundle();
            bundle.putString("country", cityDetailList.get(position).getCountry());//Data send to the DefiantionText class
            bundle.putString("region", cityDetailList.get(position).getRegion());
            bundle.putString("city", cityDetailList.get(position).getCity());
            bundle.putString("currency", cityDetailList.get(position).getCurrency());
            bundle.putString("lat", cityDetailList.get(position).getLatitude());
            bundle.putString("lon", cityDetailList.get(position).getLongitude());
            Intent intent = new Intent(this, GeoCityDetail.class);//Go to next DefiantionText class
            intent.putExtras(bundle);//Send bundle to DefiantionText class
            startActivityForResult(intent, CITY_TEXT);//Start intent

        });

    }

    public void showSnackbar(Toolbar view) {
//Showing snackbar
        final Snackbar sb = Snackbar.make(lView, "Want to exit?", Snackbar.LENGTH_LONG);
        sb.setAction("Exit", e -> finish());
        sb.show();
    }

    public void addData(String country, String region, String city, String currency, String latitude, String longitude) {//add data to database methode

        GeoSavedDataPojo geoSavedDataPojo = new GeoSavedDataPojo(country, region, city, currency, latitude, longitude);
        long id = geoDatabaseHelper.insertValue(country, region, city, currency, latitude, longitude);
        geoSavedDataPojo.setId(id);

        if (!(latitude.equals("") & longitude.equals(""))) {
            savedCityList.add(geoSavedDataPojo);
        } else {
            makeText(this, "Please enter a word", LENGTH_SHORT);
        }
    }

    public void alertMessage() {

        View middle = getLayoutInflater().inflate(R.layout.activity_geo_info, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.d_help_tittle);
        builder.setMessage(R.string.geo_info_text);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();

    }

    private class geoQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String s) {

            lView.setAdapter(cityListAdaptor);
            pBar.setProgress(100);
            pBar.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pBar.setProgress(values[0]);
        }


        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            String line = null;
            String urlString = strings[0];
            publishProgress(25);
            try {
                URL wordURL = new URL(urlString);
                urlConnection = (HttpURLConnection) wordURL.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();

                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"), 8);
                line = reader.readLine();
                publishProgress(50);
                JSONArray jsonArray = new JSONArray(line);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resultObject = jsonArray.getJSONObject(i);
                    countryJson = resultObject.getString("country");
                    regionJson = resultObject.getString("region");
                    cityJson = resultObject.getString("city");
                    currencyJson = resultObject.getString("currency_code");
                    latJson = resultObject.getString("latitude");
                    lonJson = resultObject.getString("longitude");
                    cityDetailList.add(new GeoSavedDataPojo(countryJson, regionJson, cityJson, currencyJson, latJson, lonJson));
                }
                publishProgress(75);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Finished";
        }


    }

    protected class CityListAdaptor extends BaseAdapter {
        private List<GeoSavedDataPojo> dataList;
        private Context context;
        private LayoutInflater inflater;


        public CityListAdaptor(List<GeoSavedDataPojo> dataList, Context context) {
            this.dataList = dataList;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (!dataList.get(position).equals("")) {
                view = inflater.inflate(R.layout.activity_geo_set_add, null);//Inflate view

            }


            TextView cityWord = (TextView) view.findViewById(R.id.cityText);
            cityWord.setText(dataList.get(position).getCity());//set city to list

            ImageButton imageButton = (ImageButton) view.findViewById(R.id.iButton);
            imageButton.setFocusable(false);

            if (view != null) {
                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addData(dataList.get(position).getCountry(), dataList.get(position).getRegion(), dataList.get(position).getCity(), dataList.get(position).getCurrency(), dataList.get(position).getLatitude(), dataList.get(position).getLongitude());
                        makeText(v.getContext(), "City added in list", LENGTH_SHORT).show();

                    }
                });
            }
            return view;
        }


    }
}