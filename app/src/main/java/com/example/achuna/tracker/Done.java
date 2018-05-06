package com.example.achuna.tracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Done extends AppCompatActivity {

    ListView finishedList;
    int listItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final boolean darkTheme = MainActivity.darkTheme;
        String color = MainActivity.appColor;


        if(darkTheme==true) {
            if(color.equals("Purple")) {
                setTheme(R.style.purpleDarkTheme);
            } else if (color.equals("Yellow")) {
                setTheme(R.style.yellowDarkTheme);
            } else if (color.equals("Red")) {
                setTheme(R.style.redDarkTheme);
            } else if (color.equals("Green")) {
                setTheme(R.style.greenDarkTheme);
            } else if (color.equals("Orange")) {
                setTheme(R.style.orangeDarkTheme);
            } else {
                setTheme(R.style.DarkTheme);
            }
        } else {
            if(color.equals("Purple")) {
                setTheme(R.style.purpleLightTheme);
            } else if (color.equals("Yellow")) {
                setTheme(R.style.yellowLightTheme);
            } else if (color.equals("Red")) {
                setTheme(R.style.redLightTheme);
            } else if (color.equals("Green")) {
                setTheme(R.style.greenLightTheme);
            } else if (color.equals("Orange")) {
                setTheme(R.style.orangeLightTheme);
            } else {
                setTheme(R.style.LightTheme);
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        finishedList = findViewById(R.id.finishedList);

        MainActivity.doneList = loadDoneData();

        SimpleListAdapter adapter = new SimpleListAdapter(getApplicationContext(), MainActivity.doneList, darkTheme);
        finishedList.setAdapter(adapter);

        finishedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItem = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(Done.this);
                builder.setTitle("\"" + MainActivity.doneList.get(i).getName() + "\"");
                if (darkTheme) {
                    builder.setIcon(R.drawable.check_white);
                } else {
                    builder.setIcon(R.drawable.check_black);
                }
                builder.setMessage("What would you like to do with this item?");

                builder.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        MainActivity.list.add(MainActivity.doneList.get(listItem));
                        MainActivity.doneList.remove(listItem);
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        saveDoneData();
                        SharedPreferences preferences = getSharedPreferences("Episode List", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(MainActivity.list);
                        editor.putString("List", json);
                        editor.apply();

                        startActivity(toMain);
                    }
                });

                builder.setNegativeButton("Permanently Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.doneList.remove(listItem);
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        saveDoneData();
                        startActivity(toMain);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    @Override
    protected void onStop() {
        saveDoneData();
        super.onStop();
    }

    private ArrayList<Episode> loadDoneData() {
        ArrayList<Episode> a = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("Done List", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("Done List", null);
        Type type = new TypeToken<ArrayList<Episode>>() {}.getType();
        a = gson.fromJson(json, type);
        if (a == null) a = new ArrayList<Episode>();
        return a;
    }

    private void saveDoneData() {
        SharedPreferences preferences = getSharedPreferences("Done List", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.doneList);
        editor.putString("Done List", json);
        editor.apply();
    }
}
