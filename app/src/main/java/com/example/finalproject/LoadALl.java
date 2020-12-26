package com.example.finalproject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public class LoadALl extends AsyncTask<String, String, Boolean> {

    private LatestMatchListener latestWallListener;
    private ArrayList<Match> arrayList;
    private Context mctx;

    public LoadALl(Context context, LatestMatchListener latestWallListener) {
        this.latestWallListener = latestWallListener;
        arrayList = new ArrayList<>();
        this.mctx = context;
    }

    @Override
    protected void onPreExecute() {
        latestWallListener.onStart();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        String resp = strings[0];

        try {
            Log.d("check", resp);
            JSONArray jsonArray = new JSONArray(resp);

            for (int i = 0; i < jsonArray.length(); i++) {
                Match match = (Match) new Gson().fromJson(jsonArray.getJSONObject(i).toString(), Match.class);
//                    Elements all =  match.getEmbed();
                Document doc = Jsoup.parse(match.getVideos().get(0).getEmbed());
                String link = doc.select("iframe").attr("src");

                match.setHls_url(link);
                match.setId(i);

                arrayList.add(match);
            }
            return true;
        } catch (JSONException e) {
            Toast.makeText(mctx, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            return false;
        }


    }

    @Override
    protected void onPostExecute(Boolean s) {
        latestWallListener.onEnd(String.valueOf(s), arrayList);
        super.onPostExecute(s);
    }
}