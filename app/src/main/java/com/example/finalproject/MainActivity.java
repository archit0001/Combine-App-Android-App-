package com.example.finalproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Toolbar myToolbar;
    ListView matchlist;
    ArrayList<Match> all_matches = new ArrayList<>();
    LoadALl loadALl;
    ProgressBar progressBar;
    DBHelper dbHelper;
    Boolean isLoaded = false, isVisible = false;
    MatchesAdapter matchesAdapter;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_soccer);

        dbHelper = new DBHelper(this);
        progressBar = findViewById(R.id.progressbar);
        matchlist = findViewById(R.id.list_match);
        matchesAdapter = new MatchesAdapter(this, all_matches);
        matchlist.setAdapter(matchesAdapter);

        if (!isLoaded) {
            getAllData();
//            Call_Api_For_get_Allvideos();
            isLoaded = true;
        }

    }

    private void getAllData() {
        if (isNetworkAvailable()) {
            loadALl = new LoadALl(MainActivity.this, new LatestMatchListener() {
                @Override
                public void onStart() {
                    if (all_matches.size() == 0) {
                        dbHelper.removeMatch("latest");
                        progressBar.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onEnd(String success, ArrayList<Match> arrayListWall) {

                    for (int i = 0; i < arrayListWall.size(); i++) {
                        dbHelper.addMatch(arrayListWall.get(i), "latest");
                    }
                    all_matches.addAll(arrayListWall);
                    progressBar.setVisibility(View.INVISIBLE);
                    matchesAdapter.notifyDataSetChanged();
//                            setAdapter();
                }
            });

        }
        Call_Api_For_get_Allvideos();
    }//fetching data


    public void Call_Api_For_get_Allvideos() {
        progressBar.setVisibility(View.VISIBLE);
//calling async task
        if (isNetworkAvailable()) {

            ApiRequest.Call_Api(MainActivity.this, new Const().URL, new Callback() {
                public void Responce(String str) {
//                        Parse_data(str);
                    loadALl.execute(str);
                }
            });
        } else {
            all_matches = dbHelper.getMatches("latest", "");
            matchesAdapter = new MatchesAdapter(this, all_matches);
            matchlist.setAdapter(matchesAdapter);
//            setAdapter();
//            isOver = true;
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.soccer_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.fav:
                startActivity(new Intent(MainActivity.this, Favorite.class));

//                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.about:
                alertMessage();
                Toast.makeText(getApplicationContext(), "Info", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.exit:
                showSnackbar(myToolbar);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSnackbar(Toolbar view) {
//Showing snackbar
        final Snackbar sb = Snackbar.make(matchlist, "Want to exit?", Snackbar.LENGTH_LONG);
        sb.setAction("Exit", e -> finish());
        sb.show();
    }

    public void alertMessage() {

        View middle = getLayoutInflater().inflate(R.layout.activity_soccer_help, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.d_help_tittle);
        builder.setMessage(R.string.d_help_text);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();

    }

    //adapter
    public class MatchesAdapter extends ArrayAdapter<Match> {
        public MatchesAdapter(Context context, ArrayList<Match> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Match match = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_match_soccer, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvTitle);
//            TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
            // Populate the data into the template view using the data object
            tvName.setText(match.getTitle());
//            tvHome.setText(user.hometown);
            // Return the completed view to render on screen

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Detail.class);
                    intent.putExtra("match", all_matches.get(position));
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
