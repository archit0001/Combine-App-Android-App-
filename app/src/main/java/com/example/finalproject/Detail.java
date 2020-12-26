package com.example.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.like.LikeButton;
import com.like.OnLikeListener;

public class Detail extends AppCompatActivity {

    TextView tv_team1, tv_team2, tv_date;
    Button button;
    Match match;
    LikeButton likeButton;
    DBHelper dbHelper;
    RelativeLayout rootlayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_soccer);

        dbHelper = new DBHelper(this);

        rootlayout = findViewById(R.id.rootlayout);
        tv_team1 = findViewById(R.id.home_team_name);
        tv_team2 = findViewById(R.id.away_team_name);
        tv_date = findViewById(R.id.countryname);


        button = findViewById(R.id.btn_hls);
        likeButton = findViewById(R.id.button_wall_fav);


        Bundle extras = getIntent().getExtras();
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


        likeButton.setLiked(dbHelper.isFav(String.valueOf(match.getId())));

        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                dbHelper.addtoFavorite(match);
                showSnackBar(rootlayout, "Match Added to Favorite");
                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
//                Bundle bundle=new Bundle();
//                bundle.putString("match",match.getTitle());
//                Intent intent = new Intent(Detail.this,Favorite.class);
//                intent.putExtra("title",match.getTitle());
//                startActivity(intent);
//                Toast.makeText(this, "added" ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                dbHelper.removeFav(String.valueOf(match.getId()));
                showSnackBar(rootlayout, "Match removed from favorite");
//                Toast.makeText(context, context.getString(R.string.removed_from_fav), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void showSnackBar(View linearLayout, String message) {
        Snackbar snackbar = Snackbar.make(linearLayout, message, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundResource(R.color.colorPrimaryDark);
        snackbar.show();
    }
}
