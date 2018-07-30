package com.example.achuna.tracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Planning extends AppCompatActivity {

    ListView watchList;
    int listItem = 0;
    SQLiteHandler database;

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
        setContentView(R.layout.activity_planning);

        database = new SQLiteHandler(this, null, null, 1);
        loadAllData();


        watchList = findViewById(R.id.planList);
        SimpleListAdapter adapter = new SimpleListAdapter(getApplicationContext(), MainActivity.planList, darkTheme);
        watchList.setAdapter(adapter);

        watchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listItem = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(Planning.this);
                builder.setTitle("\"" + MainActivity.planList.get(i).getName() + "\"");
                if (darkTheme) {
                    builder.setIcon(R.drawable.lightbulb_outline_white);
                } else {
                    builder.setIcon(R.drawable.lightbulb_outline_black);
                }
                builder.setMessage("Are your ready to start watching \"" + MainActivity.planList.get(i).getName() + "\"? ");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        MainActivity.planList.get(listItem).setListId(1);
                        database.updateShow(MainActivity.planList.get(listItem));
                        MainActivity.list.add(MainActivity.planList.get(listItem));
                        MainActivity.planList.remove(listItem);
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);

                        saveAllData(MainActivity.list, MainActivity.planList, MainActivity.doneList);

                        startActivity(toMain);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

    }

    public void backup() {

        ArrayList<DataObject> shows = new ArrayList<>();
        for (int i = 0; i < MainActivity.list.size(); i++) {
            int notifications = (MainActivity.list.get(i).getNotifications())? 1:0;
            shows.add(new DataObject(MainActivity.list.get(i).getName(), MainActivity.list.get(i).getNumber(), MainActivity.list.get(i).getUrl(), notifications, MainActivity.list.get(i).getTime().getDay(), MainActivity.list.get(i).getTime().getHour(), MainActivity.list.get(i).getTime().getTimeOfDay(),
                    MainActivity.list.get(i).getTime().getTimePreview(), MainActivity.list.get(i).getId(), MainActivity.list.get(i).getListId()));
        }
        for (int i = 0; i < MainActivity.planList.size(); i++) {
            int notifications = (MainActivity.planList.get(i).getNotifications())? 1:0;
            shows.add(new DataObject(MainActivity.planList.get(i).getName(), MainActivity.planList.get(i).getNumber(), MainActivity.planList.get(i).getUrl(), notifications, MainActivity.planList.get(i).getTime().getDay(), MainActivity.planList.get(i).getTime().getHour(), MainActivity.planList.get(i).getTime().getTimeOfDay(),
                    MainActivity.planList.get(i).getTime().getTimePreview(), MainActivity.planList.get(i).getId(), MainActivity.planList.get(i).getListId()));
        }
        for (int i = 0; i < MainActivity.doneList.size(); i++) {
            int notifications = (MainActivity.doneList.get(i).getNotifications())? 1:0;
            shows.add(new DataObject(MainActivity.doneList.get(i).getName(), MainActivity.doneList.get(i).getNumber(), MainActivity.doneList.get(i).getUrl(), notifications, MainActivity.doneList.get(i).getTime().getDay(), MainActivity.doneList.get(i).getTime().getHour(), MainActivity.doneList.get(i).getTime().getTimeOfDay(),
                    MainActivity.doneList.get(i).getTime().getTimePreview(), MainActivity.doneList.get(i).getId(), MainActivity.doneList.get(i).getListId()));
        }

        new DatabaseBackup(Planning.this, new DatabaseBackup.AsyncResponse() {
            @Override
            public void processFinished(boolean result) {
                if (result) {
                    SharedPreferences backup = getSharedPreferences("Backup Time", MODE_PRIVATE);
                    SharedPreferences.Editor editor = backup.edit();

                    Calendar calendar = Calendar.getInstance();
                    String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    String time = simpleDateFormat.format(calendar.getTime());

                    String date = currentDate + " at " + time;

                    editor.putString("time", date);
                    editor.apply();
                }
            }
        }).execute(shows);
    }

    @Override
    protected void onStop() {
        saveAllData(MainActivity.list, MainActivity.planList, MainActivity.doneList);
        backup();
        super.onStop();
    }

    public void loadAllData() {
        Cursor data = database.getData();
        MainActivity.list.clear();
        MainActivity.planList.clear();
        MainActivity.doneList.clear();
        while(data.moveToNext()) {
            if(data.getInt(10) == 1) {
                MainActivity.list.add(new Episode(data.getString(1), data.getInt(2), data.getString(3), (data.getInt(4) > 0),
                        new Time(data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8)), data.getInt(9), data.getInt(10)));
            } else if(data.getInt(10) == 2) {
                MainActivity.planList.add(new Episode(data.getString(1), data.getInt(2), data.getString(3), (data.getInt(4) > 0),
                        new Time(data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8)), data.getInt(9), data.getInt(10)));
            } else {
                MainActivity.doneList.add(new Episode(data.getString(1), data.getInt(2), data.getString(3), (data.getInt(4) > 0),
                        new Time(data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8)), data.getInt(9), data.getInt(10)));
            }
        }
    }

    public void saveAllData(ArrayList<Episode> a, ArrayList<Episode> b, ArrayList<Episode> c) {
        if (((a.size() + b.size() + c.size()) > 0)) database.clearShows();
        for (int i = 0; i < a.size(); i++) {
            database.addShow(a.get(i));
        }
        for (int i = 0; i < b.size(); i++) {
            database.addShow(b.get(i));
        }
        for (int i = 0; i < c.size(); i++) {
            database.addShow(c.get(i));
        }
    }

}
