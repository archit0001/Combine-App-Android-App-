package com.example.finalproject;


import java.util.ArrayList;

public interface LatestMatchListener {
    void onStart();

    void onEnd(String success, ArrayList<Match> arrayListCat);
}
