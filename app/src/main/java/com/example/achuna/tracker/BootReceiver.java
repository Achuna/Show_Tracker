package com.example.achuna.tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Achuna on 3/4/2018.
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (MainActivity.list.size() > 0) {
                for(int i = 0; i < MainActivity.list.size(); i++) {
                    if (MainActivity.list.get(i).getNotifications()) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(System.currentTimeMillis());
                        calendar.set(Calendar.HOUR_OF_DAY, MainActivity.list.get(i).getTime().getHour());
                        //calendar.set(Calendar.MINUTE, 24);
                        calendar.set(Calendar.DAY_OF_WEEK, MainActivity.list.get(i).getTime().getDay());

                        Intent bootIntent = new Intent(context, NotificationReceiver.class);
                        intent.putExtra("showName", MainActivity.list.get(i).getName());
                        intent.putExtra("showNumber", MainActivity.list.get(i).getNumber());
                        intent.putExtra("showUrl", MainActivity.list.get(i).getUrl());
                        intent.putExtra("showIndex", MainActivity.list.indexOf(MainActivity.list.get(i)));
                        intent.putExtra("id", MainActivity.list.get(i).getId());

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, MainActivity.list.get(i).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        MainActivity.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
                    }
                }
            }
        }
    }
}
