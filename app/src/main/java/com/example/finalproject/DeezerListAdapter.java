package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DeezerListAdapter extends BaseAdapter {

    private List<DeezerSavedDataPojo> dataList;
    private Context context;
    private LayoutInflater inflater;

    public DeezerListAdapter(List<DeezerSavedDataPojo> dataList, Context context) {
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
            view = inflater.inflate(R.layout.activity_set_songs_deezer, null);//Inflate view

        }


        TextView songTitle = (TextView) view.findViewById(R.id.savedSongText);
        songTitle.setText(dataList.get(position).title);//set title to list

        return view;
    }


}


