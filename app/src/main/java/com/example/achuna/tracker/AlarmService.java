package com.example.achuna.tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Achuna on 3/10/2018.
 *
 * Repeatedly Sets the alarm for each show
 */

public class AlarmService extends Service {
    private boolean isRunning;
    private Context context;
    private Thread backgroundThread;
    private SQLiteHandler database;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
        this.isRunning = false;
        database = new SQLiteHandler(context, null, null, 1);
        this.backgroundThread = new Thread(myTask);

    }

    //This tasks will run in the background and set the alarms every 30 minutes
    private Runnable myTask = new Runnable() {
        public void run() {

            ArrayList<Episode> list = new ArrayList<>();
            Cursor data = database.getData();
            while(data.moveToNext()) {
                if(data.getInt(10) == 1) {
                    list.add(new Episode(data.getString(1), data.getInt(2), data.getString(3), (data.getInt(4) > 0),
                            new Time(data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8)), data.getInt(9), data.getInt(10)));
                }
            }

//            SharedPreferences preferences = context.getSharedPreferences("Episode List", MODE_PRIVATE);
//            Gson gson = new Gson();
//            String json = preferences.getString("List", null);
//            Type type = new TypeToken<ArrayList<Episode>>() {}.getType();
//            list = gson.fromJson(json, type);
//            if (list == null) list = new ArrayList<Episode>();


            //////////////SET ALARMS WHEN DEVICE BOOTS UP//////////

            for (int i = 0; i < list.size(); i++) {

                if(list.get(i).getNotifications()) {

                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                    Calendar now = Calendar.getInstance();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());

                    calendar.set(Calendar.HOUR_OF_DAY, list.get(i).getTime().getHour());
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.DAY_OF_WEEK, list.get(i).getTime().getDay());

                    Intent alarmIntent = new Intent(context, NotificationReceiver.class);
                    alarmIntent.putExtra("showName", list.get(i).getName());
                    alarmIntent.putExtra("showNumber", list.get(i).getNumber());
                    alarmIntent.putExtra("showUrl", list.get(i).getUrl());
                    alarmIntent.putExtra("id", list.get(i).getId());

                    long weeklyInterval = 1000 * 60 * 60 * 24 * 7;
                    long diff = now.getTimeInMillis() - calendar.getTimeInMillis();
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, list.get(i).getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    if (diff > 0) {
                        alarmManager.cancel(pendingIntent);
                    } else {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), weeklyInterval, pendingIntent);

                    }
                }
            }

            Log.i("AService", "Alarm Service Ran ----------------------------------------------");

            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}

