package com.example.achuna.tracker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Achuna on 2/24/2018.
 */

public class ColorSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    int[] colorImages;
    String[] colorNames;
    boolean darkTheme;

    public ColorSpinnerAdapter(@NonNull Context context, int[] colorImages, String[] colorNames, boolean darkTheme) {
        super(context, R.layout.color_spinner);
        this.colorImages = colorImages;
        this.colorNames = colorNames;
        this.darkTheme = darkTheme;
    }

    @Override
    public int getCount() {
        return colorNames.length;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.color_spinner, parent, false);

        viewHolder.img = convertView.findViewById(R.id.colorImage);
        viewHolder.name = convertView.findViewById(R.id.colorName);

        viewHolder.img.setImageResource(colorImages[position]);
        viewHolder.name.setText(colorNames[position]);
        if(darkTheme) {
            viewHolder.name.setTextColor(Color.WHITE);
        } else {
            viewHolder.name.setTextColor(Color.BLACK);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView img;
        TextView name;
    }
}
