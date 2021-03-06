package com.example.achuna.tracker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Achuna on 2/23/2018.
 */

public class SimpleListAdapter extends ArrayAdapter<Episode> {

    Context context;
    ArrayList<Episode> list;
    boolean darkTheme;

    public SimpleListAdapter(@NonNull Context context, ArrayList<Episode> list, boolean darkTheme) {
        super(context, R.layout.simple_list_layout);
        this.list = list;
        this.darkTheme = darkTheme;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.simple_list_layout, parent, false);

        ViewHolder holder = new ViewHolder();
        holder.showName = convertView.findViewById(R.id.doneEpisodeName);
        holder.showNumber = convertView.findViewById(R.id.doneEpisodNumber);

        holder.showName.setText(list.get(position).getName());
        holder.showNumber.setText(list.get(position).getNumber()+"");

        if(darkTheme) {
            holder.showName.setTextColor(Color.WHITE);
            holder.showNumber.setTextColor(Color.WHITE);
        } else {
            holder.showName.setTextColor(Color.BLACK);
            holder.showNumber.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView showName, showNumber;
    }
}
