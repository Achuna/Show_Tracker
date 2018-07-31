package com.example.achuna.tracker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Achuna on 2/20/2018.
 */

public class EpisodeListAdapter extends ArrayAdapter<String> {

    Context context;
    ArrayList<Episode> shows;
    boolean darkTheme;
    SQLiteHandler database;

    public EpisodeListAdapter(@NonNull Context context, ArrayList<Episode> show, boolean darkTheme) {
        super(context, R.layout.episode_list);
        this.shows = show;
        this.darkTheme = darkTheme;
        this.context = context;
        database = new SQLiteHandler(context, null, null, 1);
    }

    @Override
    public int getCount() {
        return shows.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.episode_list, parent, false);

        viewHolder.episodeName = convertView.findViewById(R.id.listAdapterShowName);
        viewHolder.episodeNumber = convertView.findViewById(R.id.listAdapterNumber);
        viewHolder.add = convertView.findViewById(R.id.addButton);
        viewHolder.minus = convertView.findViewById(R.id.minusButton);

        if(darkTheme) {
            viewHolder.episodeName.setText(MainActivity.list.get(position).getName());
            viewHolder.episodeName.setTextColor(Color.WHITE);
            viewHolder.episodeNumber.setText(MainActivity.list.get(position).getNumber() + "");
            viewHolder.episodeNumber.setTextColor(Color.WHITE);

        } else {
            viewHolder.episodeName.setText(MainActivity.list.get(position).getName());
            viewHolder.episodeName.setTextColor(Color.BLACK);
            viewHolder.episodeNumber.setText(MainActivity.list.get(position).getNumber() + "");
            viewHolder.episodeNumber.setTextColor(Color.BLACK);
        }

        viewHolder.add.setImageResource(R.drawable.episode_add_image);
        viewHolder.minus.setImageResource(R.drawable.episode_minus_image);

        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.list.get(position).setNumber(MainActivity.list.get(position).getNumber() + 1);
                viewHolder.episodeNumber.setText(MainActivity.list.get(position).getNumber() + "");
                database.updateShow(MainActivity.list.get(position));
            }
        });

        viewHolder.add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MainActivity.list.get(position).setNumber(MainActivity.list.get(position).getNumber() + 5);
                viewHolder.episodeNumber.setText(MainActivity.list.get(position).getNumber() + "");
                database.updateShow(MainActivity.list.get(position));
                return true;
            }
        });
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.list.get(position).getNumber() >= 1) {
                    MainActivity.list.get(position).setNumber(MainActivity.list.get(position).getNumber() - 1);
                    viewHolder.episodeNumber.setText(MainActivity.list.get(position).getNumber() + "");
                    database.updateShow(MainActivity.list.get(position));
                }
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView episodeName, episodeNumber;
        ImageButton add, minus;
    }
}
