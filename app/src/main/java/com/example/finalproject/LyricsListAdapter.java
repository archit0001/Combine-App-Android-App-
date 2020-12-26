package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LyricsListAdapter extends BaseAdapter {

    private List<LyricsSavedDataPojo> dataList;
    private Context context;
    private LayoutInflater inflater;

    //constructor
    public LyricsListAdapter(List<LyricsSavedDataPojo> dataList, Context context) {
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


        if (dataList.get(position).getId() >= 0) {
            view = inflater.inflate(R.layout.lyrics_set_artist_title, null);//Inflate view

        }


        TextView setArtist = (TextView) view.findViewById(R.id.artistTitle);
        setArtist.setText(dataList.get(position).artist + "  :  " + dataList.get(position).title);//set Artist and title to list

        return view;
    }


}


