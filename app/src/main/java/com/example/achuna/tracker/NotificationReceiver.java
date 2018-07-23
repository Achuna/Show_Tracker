package com.example.achuna.tracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Achuna on 3/2/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle extras = intent.getExtras();

        String name = extras.getString("showName");
       // Log.i("Receiver", name);
        int number = extras.getInt("showNumber");
       // Log.i("Receiver", number+"");
        String url = extras.getString("showUrl");
       // Log.i("Receiver", url+"");
        int listItem = 0;
        try {
            listItem = extras.getInt("showIndex");
            Log.i("Receiver", listItem + "");
        } catch (Exception e) {
            //Not need if device reboots
        }
        int id = extras.getInt("id");
       // Log.i("Receiver", id+"");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent startMain = new Intent(context, MainActivity.class);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, startMain, PendingIntent.FLAG_UPDATE_CURRENT);




        //For Personal Use
        //https://otakustream.tv/anime/dragon-ball-super/episode-129/
        String specificUrl = url;
        if (url != null) {
            if(url.toLowerCase().contains("anime")) {
                specificUrl = url + (number + 1) + "/";
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", specificUrl);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "URL Copied", Toast.LENGTH_SHORT).show();
            }
        }

        context.getPackageManager().clearPackagePreferredActivities(context.getPackageName());
        Intent stream = new Intent(Intent.ACTION_VIEW);

        stream.setData(Uri.parse(specificUrl));
        Intent chooser = Intent.createChooser(stream, "Select Browser");

        //stream.putExtra("url", specificUrl); //change to specificUrl to url on deployment
        //stream.putExtra("index", listItem);
        PendingIntent watchIntent = PendingIntent.getActivity(context, id, chooser, PendingIntent.FLAG_UPDATE_CURRENT);







        ///////////////Build Notification/////////////

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentIntent(pendingIntent);

        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setSubText(MainActivity.listTitle);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.self));
        builder.setContentTitle("New Release!");

        if(name == null) {
            builder.setContentText("Tap to Open Tracker");
        } else {
            builder.setContentText("\""+name +"\"");
            builder.setStyle(new NotificationCompat.InboxStyle().addLine("\"" +name+"\"").addLine("Episode " + (number+1) + " is out!"));
        }


        //Add "Watch Now" Action
        try {
            if (url.length() > 0 && URLUtil.isValidUrl(specificUrl)) {
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
