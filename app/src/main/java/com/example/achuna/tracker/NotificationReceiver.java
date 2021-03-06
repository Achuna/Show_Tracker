package com.example.achuna.tracker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Toast;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * Created by Achuna on 3/2/2018.
 */

public class NotificationReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        stream.setPackage("com.hsv.freeadblockerbrowser");

        //stream.putExtra("url", specificUrl); //change to specificUrl to url on deployment
        //stream.putExtra("index", listItem);
        PendingIntent watchIntent = PendingIntent.getActivity(context, id, stream, PendingIntent.FLAG_UPDATE_CURRENT);

        ///////////////////Create Notification Channel for Android O and above (REQUIRED)/////////////////

        String cid = "MY_Channel";
        String ctitle = "Achuna's Notification Channel";


        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationChannel mChannel = mNotificationManager.getNotificationChannel(cid);

            mChannel = new NotificationChannel(cid, ctitle, NotificationManager.IMPORTANCE_HIGH);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{0, 350, 350});
        mNotificationManager.createNotificationChannel(mChannel);

        mNotificationManager.createNotificationChannel(mChannel);


        ///////////////Build Notification/////////////

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, cid);

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
