package com.example.achuna.tracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Achuna on 3/2/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String name = intent.getExtras().getString("showName");
        Log.i("Receiver", name);
        int number = intent.getExtras().getInt("showNumber");
        Log.i("Receiver", number+"");
        String url = intent.getExtras().getString("showUrl");
        Log.i("Receiver", url+"");
        int listItem = intent.getExtras().getInt("showIndex");
        Log.i("Receiver", listItem+"");
        int id = intent.getExtras().getInt("id");
        Log.i("Receiver", id+"");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent startMain = new Intent(context, MainActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, startMain, PendingIntent.FLAG_UPDATE_CURRENT);


        Intent stream = new Intent(context, StreamActivity.class);

        //For Personal Use
        //https://otakustream.tv/anime/dragon-ball-super/episode-129/
        String specificUrl = url + (number + 1) + "/";

        stream.putExtra("url", specificUrl); //change to specificUrl to url on deployment
        stream.putExtra("index", listItem);
        PendingIntent watchIntent = PendingIntent.getActivity(context, id, stream, PendingIntent.FLAG_UPDATE_CURRENT);


        ///////////////Build Notification/////////////

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentIntent(pendingIntent);

        builder.setSmallIcon(R.drawable.small_icon_white);
        builder.setSubText(MainActivity.listTitle);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.self));
        builder.setContentTitle("New Release!");
        builder.setContentText("\""+name +"\"");
        builder.setStyle(new NotificationCompat.InboxStyle().addLine("\"" +name+"\"").addLine("Episode " + (number+1) + " is out!"));


        //Add Watch Action
        try {
            if (MainActivity.list.get(listItem).getUrl().length() > 0) {

                builder.addAction(Color.TRANSPARENT, "Watch Now", watchIntent);
            }

        } catch (Exception e) {
            //Show notification without "watch option"
        }


        //Notify elements
        builder.setVibrate(new long[]{0, 350, 350});
        builder.setLights(Color.WHITE, 2000, 2000);
        builder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        notificationManager.notify(id+3, builder.build());

    }
}
