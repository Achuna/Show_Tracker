package com.example.achuna.tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Achuna on 3/4/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {



        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent startAlarmService = new Intent(context, AlarmService.class);
            context.startService(startAlarmService);
            Log.i("AService", "Started Alarm Service From Device Boot--------------------------------------------");

//            ArrayList<Episode> list = new ArrayList<>();
//            SharedPreferences preferences = context.getSharedPreferences("Episode List", MODE_PRIVATE);
//            Gson gson = new Gson();
//            String json = preferences.getString("List", null);
//            Type type = new TypeToken<ArrayList<Episode>>() {
//            }.getType();
//            list = gson.fromJson(json, type);
//            if (list == null) list = new ArrayList<Episode>();
//
//
//            //////////////SET ALARMS WHEN DEVICE BOOTS UP//////////
//
//            for (int i = 0; i < list.size(); i++) {
//
//                if(list.get(i).notifications) {
//
//                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
//
//                    Calendar now = Calendar.getInstance();
//                    Calendar calendar = Calendar.getInstance();
//                    calendar.setTimeInMillis(System.currentTimeMillis());
//
//                    calendar.set(Calendar.HOUR_OF_DAY, list.get(i).getTime().getHour());
//                    calendar.set(Calendar.MINUTE, 0);
//                    calendar.set(Calendar.SECOND, 0);
//                    calendar.set(Calendar.DAY_OF_WEEK, list.get(i).getTime().getDay());
//
//                    Intent alarmIntent = new Intent(context, NotificationReceiver.class);
//                    alarmIntent.putExtra("showName", list.get(i).getName());
//                    alarmIntent.putExtra("showNumber", list.get(i).getNumber());
//                    alarmIntent.putExtra("showUrl", list.get(i).getUrl());
//                    alarmIntent.putExtra("id", list.get(i).getId());
//
//                    long weeklyInterval = 1000 * 60 * 60 * 24 * 7;
//                    long diff = now.getTimeInMillis() - calendar.getTimeInMillis();
//                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, list.get(i).getId(), alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    if (diff > 0) {
//                        alarmManager.cancel(pendingIntent);
//                    } else {
//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), weeklyInterval, pendingIntent);
//
//                    }
//                }
//                }
           }




    }
}
