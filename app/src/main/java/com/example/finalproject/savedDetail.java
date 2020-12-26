package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class savedDetail extends AppCompatActivity {
    TextView tv_team1, tv_team2, tv_date;
    Button button;
    Match match;
    DBHelper dbHelper;
    RelativeLayout rootlayout;
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_detail_soccer);

        dbHelper = new DBHelper(this);
        deleteButton = (FloatingActionButton) findViewById(R.id.delete);
        rootlayout = findViewById(R.id.rootlayout);
        tv_team1 = findViewById(R.id.home_team_name);
        tv_team2 = findViewById(R.id.away_team_name);
        tv_date = findViewById(R.id.countryname);

        button = findViewById(R.id.btn_hls);

        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            match = (Match) getIntent().getSerializableExtra("match"); //Obtaining data
        }

        tv_team1.setText(match.getSide1().getName());
        tv_team2.setText(match.getSide2().getName());
        tv_date.setText(match.getDate());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(match.getHls_url()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToActivity = new Intent();
                backToActivity.putExtra("id", match.getId());//set value for Key id
                backToActivity.putExtra("position", extras.getInt("position"));
                savedDetail.this.setResult(Activity.RESULT_OK, backToActivity);//send data back to SavedResult in onActivityResult()
                savedDetail.this.finish();//Go to back Activity
            }
        });
    }
}