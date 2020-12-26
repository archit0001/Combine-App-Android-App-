package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Favorite extends AppCompatActivity {


    public static final int SAVED_MATCH_TEXT = 21;
    ArrayList<String> all_matches = new ArrayList<>();
    ArrayList<Match> all_matches_obj = new ArrayList<>();
    DBHelper dbHelper;
    private ListView savedlist;
    private ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_soccer);

        dbHelper = new DBHelper(this);
        savedlist = findViewById(R.id.favList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, all_matches);
        Bundle bundle = getIntent().getExtras();
        all_matches_obj = dbHelper.getFavMatches("fav", "");
        for (int i = 0; i < all_matches_obj.size(); i++)
            all_matches.add(all_matches_obj.get(i).getTitle());
        savedlist.setAdapter(arrayAdapter);

        savedlist.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            Intent intent = new Intent(Favorite.this, savedDetail.class);
            intent.putExtra("match", all_matches_obj.get(position));
            intent.putExtra("position", position);
            startActivityForResult(intent, SAVED_MATCH_TEXT);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SAVED_MATCH_TEXT) {//get requestCode from DefiantionText class
            if (resultCode == RESULT_OK) {
                String id = String.valueOf(data.getIntExtra("id", 0));//get value for key id
                int position = data.getIntExtra("position", 0);//get value for key position
                deleteMatchId(id, position);//Delete ID
            }
        }

    }

    public void deleteMatchId(String id, int position) {
        dbHelper.removeFav(id);//Delete id from Database
        all_matches.remove(position);//Remove from list
        arrayAdapter.notifyDataSetChanged();//Update Adapter view

    }
}
