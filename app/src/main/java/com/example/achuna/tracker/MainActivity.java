package com.example.achuna.tracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final String TAG = "MainActivity";

    Toolbar toolbar;
    ListView episodeList;
    Button addShow;
    TextView title, drawerTitle;

    DrawerLayout sideBar;
    ActionBarDrawerToggle sideBarToggle;
    NavigationView navigationView;

    static AlarmManager alarmManager;

    //All the List to be used throughout project
    static ArrayList<Episode> list = new ArrayList<>();
    static ArrayList<Episode> doneList = new ArrayList<>();
    static ArrayList<Episode> planList = new ArrayList<>();

    static String toolbarTitle = "Tracker",listTitle ="Episode List",appColor ="Blue";
    static boolean darkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences theme = getSharedPreferences("App Theme", MODE_PRIVATE);
        MainActivity.darkTheme = theme.getBoolean("main theme", false);
        SharedPreferences colorPref = getSharedPreferences("App Color", MODE_PRIVATE);
        MainActivity.appColor = colorPref.getString("main color", "Blue");
        String color = MainActivity.appColor;

        Log.i("Color", MainActivity.appColor);
        if (darkTheme == true) {
            if (color.equals("Purple")) {
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
            if (color.equals("Purple")) {
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
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.main_toolbar);
        sideBar = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        if (darkTheme == true) {
            if (color.equals("Purple")) {
                toolbar.setBackgroundColor(Color.parseColor("#401e56"));
            } else if (color.equals("Yellow")) {
                toolbar.setBackgroundColor(Color.parseColor("#FFEB3B"));
            } else if (color.equals("Red")) {
                toolbar.setBackgroundColor(Color.parseColor("#740600"));
            } else if (color.equals("Green")) {
                toolbar.setBackgroundColor(Color.parseColor("#137400"));
            } else if (color.equals("Orange")) {
                toolbar.setBackgroundColor(Color.parseColor("#56481e"));
            } else {
                toolbar.setBackgroundColor(Color.parseColor("#1e2756"));
            }
        } else {
            if (color.equals("Purple")) {
                toolbar.setBackgroundColor(Color.parseColor("#9000ff"));
            } else if (color.equals("Yellow")) {
                toolbar.setBackgroundColor(Color.parseColor("#FFEB3B"));
            } else if (color.equals("Red")) {
                toolbar.setBackgroundColor(Color.parseColor("#ff3e3b"));
            } else if (color.equals("Green")) {
                toolbar.setBackgroundColor(Color.parseColor("#40e404"));
            } else if (color.equals("Orange")) {
                toolbar.setBackgroundColor(Color.parseColor("#ffb300"));
            } else {
                toolbar.setBackgroundColor(Color.parseColor("#006aff"));
            }
        }

        SharedPreferences settings = getSharedPreferences("Titles", MODE_PRIVATE);
        toolbarTitle = settings.getString("header", toolbarTitle);
        listTitle = settings.getString("list title", listTitle);

        View header = navigationView.getHeaderView(0);
        drawerTitle = header.findViewById(R.id.drawerTitle);
        drawerTitle.setText(listTitle);
        navigationView.setNavigationItemSelectedListener(this);

        sideBarToggle = new ActionBarDrawerToggle(this, sideBar, toolbar, R.string.open, R.string.close);
        sideBarToggle.setDrawerSlideAnimationEnabled(false);
        sideBar.addDrawerListener(sideBarToggle);
        sideBarToggle.syncState();

        toolbar.setTitle(toolbarTitle);
        setSupportActionBar(toolbar);

        addShow = findViewById(R.id.addShowButton);
        episodeList = findViewById(R.id.episodeListView);
        title = findViewById(R.id.episodeMainTitle);
        title.setText(listTitle);

        EpisodeListAdapter adapter = new EpisodeListAdapter(getApplicationContext(), loadListData(), darkTheme);
        list = loadListData();
        episodeList.setAdapter(adapter);


        episodeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent startEditor = new Intent(getApplicationContext(), EditorActivity.class);
                startEditor.putExtra("Editing", true);
                startEditor.putExtra("ListItem", i);
                startActivity(startEditor);
            }
        });


        addShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startEditor = new Intent(getApplicationContext(), EditorActivity.class);
                startEditor.putExtra("Editing", false);
                startActivity(startEditor);
            }
        });

    }


    @Override
    protected void onStop() {

        saveListData(list);
        super.onStop();
    }


    public void cancelAlarm(Episode show) {
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("showName", show.getName());
        intent.putExtra("showNumber", show.getNumber());
        intent.putExtra("showUrl", show.getUrl());
        intent.putExtra("showIndex", list.indexOf(show));
        intent.putExtra("id", show.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), show.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public void setAlarm(Episode show) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar now = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hour = show.getTime().getHour();
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        //Toast.makeText(getApplicationContext(), "Item: " + list.indexOf(show) + "\nDay: "+show.getTime().getDay() + " Hour: "+show.getTime().getHour()+"", Toast.LENGTH_LONG).show();
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        //calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, show.getTime().getDay());

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("showName", show.getName());
        intent.putExtra("showNumber", show.getNumber());
        intent.putExtra("showUrl", show.getUrl());
        intent.putExtra("showIndex", list.indexOf(show));
        intent.putExtra("id", show.getId());

        long weeklyInterval = 1000 * 60 * 60 * 24 * 7;
        //long interval = 1000 * 10;

        long diff = now.getTimeInMillis() - calendar.getTimeInMillis();
        if (diff > 0) {
            calendar.add(Calendar.DAY_OF_WEEK, 7);
            // Toast.makeText(getApplicationContext(), "Item: " + list.indexOf(show) + " \nAdded Week", Toast.LENGTH_SHORT).show();
        }


        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), show.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (pendingIntent == null) {
            Toast.makeText(getApplicationContext(), "Notification", Toast.LENGTH_SHORT).show();
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), weeklyInterval, pendingIntent);
        //Toast.makeText(getApplicationContext(), "Item: " + list.indexOf(show) + "\nDay: "+show.getTime().getDay() + " Hour: "+show.getTime().getHour()+"", Toast.LENGTH_LONG).show();


       // alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

    }


    public void saveListData(ArrayList<Episode> a) {
        //Converting arraylist into json format
        SharedPreferences preferences = getSharedPreferences("Episode List", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(a);
        editor.putString("List", json);
        editor.apply();
    }

    public ArrayList<Episode> loadListData() {
        ArrayList<Episode> a = new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("Episode List", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preferences.getString("List", null);
        Type type = new TypeToken<ArrayList<Episode>>() {
        }.getType();
        a = gson.fromJson(json, type);
        if (a == null) a = new ArrayList<Episode>();
        return a;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (sideBarToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.menuSettings:
                Intent settingsIntent = new Intent(getApplicationContext(), Settings.class);
                SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Header", MainActivity.toolbarTitle);
                editor.putString("List Title", MainActivity.listTitle);
                editor.apply();
                startActivity(settingsIntent);
                break;
            case R.id.menuKeep:
                //open keep
                Intent openKeep = getPackageManager().getLaunchIntentForPackage("com.google.android.keep");
                if (openKeep != null) {
                    startActivity(openKeep);
                } else {
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://keep.google.com"));
                    if (openBrowser != null) {
                        startActivity(openBrowser);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to Open Keep", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.menuDone:
                Intent finishIntent = new Intent(getApplicationContext(), Done.class);
                startActivity(finishIntent);
                break;
            case R.id.menuPlan:
                Intent toWatchLater = new Intent(getApplicationContext(), Planning.class);
                startActivity(toWatchLater);
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawerSettings:
                sideBar.closeDrawers();
                Intent settingsIntent = new Intent(getApplicationContext(), Settings.class);
                SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Header", MainActivity.toolbarTitle);
                editor.putString("List Title", MainActivity.listTitle);
                editor.apply();
                startActivity(settingsIntent);
                break;
            case R.id.drawerKeep:
                //open keep
                sideBar.closeDrawers();
                Intent openKeep = getPackageManager().getLaunchIntentForPackage("com.google.android.keep");
                if (openKeep != null) {
                    startActivity(openKeep);
                } else {
                    Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://keep.google.com"));
                    if (openBrowser != null) {
                        startActivity(openBrowser);
                    } else {
                        Toast.makeText(getApplicationContext(), "Unable to Open Keep", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.drawerFinished:
                sideBar.closeDrawers();
                Intent finishIntent = new Intent(getApplicationContext(), Done.class);
                startActivity(finishIntent);
                break;
            case R.id.drawerAlarm:
                //Set Appropriate Alarms
                if (list.size() > 0) {
                    for(int i = 0; i < list.size(); i++) {
                        if (list.get(i).getNotifications()) {
                            setAlarm(list.get(i));
                        } else {
                            cancelAlarm(list.get(i));
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Alarms Set", Toast.LENGTH_SHORT).show();
                }
                int size = navigationView.getMenu().size();
                for (int i = 0; i < size; i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                    sideBar.closeDrawers();
                }
                break;
            case R.id.drawerWatchLater:
                sideBar.closeDrawers();
                Intent toWatchLater = new Intent(getApplicationContext(), Planning.class);
                startActivity(toWatchLater);
                break;
        }
        return true;
    }

    @Override
    protected void onRestart() {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        super.onRestart();
    }
}
