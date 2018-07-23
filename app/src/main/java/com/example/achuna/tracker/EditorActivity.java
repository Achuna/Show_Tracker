package com.example.achuna.tracker;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class EditorActivity extends Activity {

    CheckBox notifyCheckbox;
    TextView notificationTime, atText;
    EditText showName, episodeNumber, urlText;
    Button saveBtn, deleteBtn, doneBtn, laterBtn, watchNow;
    ListView dayList, timeList;
    int listItem = 0;
    boolean editingExitingShow = false;
    Time time = new Time(1, 1, 1, "");
    Time timePreview = new Time(1, 1, 1, "");

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
        setContentView(R.layout.activity_editor);

        //Initialize Views


        notifyCheckbox = findViewById(R.id.notifyCheckbox);
        notificationTime = findViewById(R.id.notificationTime);
        atText = findViewById(R.id.atDivider);
        saveBtn = findViewById(R.id.editorSaveBtn);
        deleteBtn = findViewById(R.id.deleteButton);
        doneBtn = findViewById(R.id.doneButton);
        laterBtn = findViewById(R.id.planToWatchButton);
        watchNow = findViewById(R.id.watchNow);

        showName = findViewById(R.id.showNameEt);
        episodeNumber = findViewById(R.id.showNumber);
        urlText = findViewById(R.id.urlText);


        dayList = findViewById(R.id.dayList);
        timeList = findViewById(R.id.timeList);


        dayList.setAdapter(new dayListAdapter(EditorActivity.this, getDays(), darkTheme));
        timeList.setAdapter(new dayListAdapter(EditorActivity.this, getHours(), darkTheme));

//how to stay in a quiet room with the most annoy teacher in the how can I be so quiet and why am in this room with this person
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editingExitingShow = Boolean.parseBoolean(extras.get("Editing").toString());
            if(editingExitingShow) {
                listItem = (int) extras.get("ListItem");
                if(MainActivity.list.get(listItem).notifications)
                    time = MainActivity.list.get(listItem).getTime();
                Log.i("Item", extras.get("ListItem") +"");
                notifyCheckbox.setChecked(MainActivity.list.get(listItem).getNotifications());
                if(notifyCheckbox.isChecked()) time = MainActivity.list.get(listItem).getTime();
                notificationTime.setText(time.getTimePreview());
                showName.setText(MainActivity.list.get(listItem).getName());
                episodeNumber.setText(MainActivity.list.get(listItem).getNumber()+"");
                urlText.setText(MainActivity.list.get(listItem).getUrl());
                doneBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
                laterBtn.setVisibility(View.VISIBLE);
                if (urlText.getText().toString().length() > 0)
                    watchNow.setVisibility(View.VISIBLE);
                if(darkTheme) {
                    doneBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.check_white);
                    deleteBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.delete_icon_white);
                    laterBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.lightbulb_outline_white);
                } else {
                    doneBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.check_black);
                    deleteBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.delete_icon_black);
                    laterBtn.setCompoundDrawablesWithIntrinsicBounds(0,0,0, R.drawable.lightbulb_outline_black);
                }
            } else {
                doneBtn.setVisibility(View.INVISIBLE);
                deleteBtn.setVisibility(View.INVISIBLE);
                laterBtn.setVisibility(View.INVISIBLE);
                watchNow.setVisibility(View.INVISIBLE);
                atText.setVisibility(View.INVISIBLE);
            }
        }


        dayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timePreview.setDay(i+1);
                time.setDay(i+1);
                if(editingExitingShow) {
                    if(MainActivity.list.get(listItem).notifications)
                        timePreview.setHour(time.getHour());
                }
                notificationTime.setText(timePreview.toString());
                time.setTimePreview(notificationTime.getText().toString());
            }
        });

        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                timePreview.setHour(i);
                //0 for AM and 1 for PM
                time.setHour(i);
                int timeOfDay = (getHours().get(i).contains("AM")) ? 0 : 1;
                timePreview.setTimeOfDay(timeOfDay);
                if(editingExitingShow) {
                    if(MainActivity.list.get(listItem).notifications)
                        timePreview.setDay(time.getDay());
                }
                notificationTime.setText(timePreview.toString());
                time.setTimePreview(notificationTime.getText().toString());
            }
        });

        if (notifyCheckbox.isChecked()) {
            notificationTime.setVisibility(View.VISIBLE);
            dayList.setVisibility(View.VISIBLE);
            timeList.setVisibility(View.VISIBLE);
            atText.setVisibility(View.VISIBLE);
        } else {
            notificationTime.setVisibility(View.INVISIBLE);
            dayList.setVisibility(View.INVISIBLE);
            timeList.setVisibility(View.INVISIBLE);
            atText.setVisibility(View.INVISIBLE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                if(darkTheme) {
                    builder.setIcon(R.drawable.delete_icon_white);
                } else {
                    builder.setIcon(R.drawable.delete_icon_black);
                }
                builder.setTitle("Delete Conformation");
                builder.setMessage("Delete \"" + showName.getText() + "\" from your list?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "\"" + showName.getText() + "\" has been deleted", Toast.LENGTH_LONG).show();
                        MainActivity.list.remove(listItem);
                        saveData();
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Watch Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                        MainActivity.planList.add(MainActivity.list.get(listItem));
                        MainActivity.list.remove(listItem);
                        saveData();
                        SharedPreferences preferences = getSharedPreferences("Plan List", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(MainActivity.planList);
                        editor.putString("Plan List", json);
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "\""+ showName.getText() + "\" added to watch later", Toast.LENGTH_SHORT).show();
                        startActivity(startMain);
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (showName.getText().length() == 0 || episodeNumber.getText().length() == 0) {
                        if(showName.getText().length() == 0 && episodeNumber.getText().length() == 0) {
                            showName.setError("Required Field");
                            episodeNumber.setError("Required Field");
                        }
                        if (showName.getText().length() == 0) {
                            showName.setError("Required Field");
                        } else if (episodeNumber.getText().length() == 0) {
                            episodeNumber.setError("Required Field");
                        }

                    } else {
                        final Intent goBackToMain = new Intent(getApplicationContext(), MainActivity.class);

                        if(editingExitingShow == false) { //Creating a new Show

                            String name = showName.getText().toString();
                            int number = Integer.parseInt(episodeNumber.getText().toString());
                            String url = urlText.getText().toString();
                            int id = (int) System.currentTimeMillis();
                            final Episode newShow  = new Episode(name, number, url, notifyCheckbox.isChecked(), time, id);

                            AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);

                            builder.setTitle("New Show!");
                            if (darkTheme) {
                                builder.setIcon(R.drawable.lightbulb_outline_white);
                            } else {
                                builder.setIcon(R.drawable.lightbulb_outline_black);
                            }

                            builder.setMessage("What would you like to do with this item?");

                            builder.setPositiveButton("Start Tracking", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity.list.add(newShow);
                                    int newIndex = MainActivity.list.indexOf(newShow);

                                    if(newShow.notifications) {
                                        setAlarm(MainActivity.list.get(newIndex));
                                    } else {
                                        cancelAlarm(MainActivity.list.get(newIndex));
                                    }
                                    saveData();
                                    startActivity(goBackToMain);
                                }
                            });

                            builder.setNegativeButton("Watch Later", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MainActivity.list.add(newShow);
                                    int newIndex = MainActivity.list.indexOf(newShow);
                                    MainActivity.planList.add(MainActivity.list.get(newIndex));
                                    MainActivity.list.remove(newIndex);
                                    saveData();
                                    SharedPreferences preferences = getSharedPreferences("Plan List", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(MainActivity.planList);
                                    editor.putString("Plan List", json);
                                    editor.apply();
                                    Toast.makeText(getApplicationContext(), "\""+ showName.getText() + "\" added to watch later", Toast.LENGTH_SHORT).show();
                                    saveData();
                                    startActivity(goBackToMain);
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        } else { //Editing an existing show

                            //Add changes
                            MainActivity.list.get(listItem).setName(showName.getText().toString());
                            MainActivity.list.get(listItem).setNumber(Integer.parseInt(episodeNumber.getText().toString()));
                            MainActivity.list.get(listItem).setUrl(urlText.getText().toString());
                            MainActivity.list.get(listItem).setNotifications(notifyCheckbox.isChecked());
                            MainActivity.list.get(listItem).setTime(time);

                            if(MainActivity.list.get(listItem).notifications) {
                                setAlarm(MainActivity.list.get(listItem));
                            } else {
                                cancelAlarm(MainActivity.list.get(listItem));
                            }

                            saveData();
                            startActivity(goBackToMain);
                        }
                    }
            }
        });

        watchNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showName.getText().length() == 0 || episodeNumber.getText().length() == 0) {
                    if(showName.getText().length() == 0 && episodeNumber.getText().length() == 0) {
                        showName.setError("Required Field");
                        episodeNumber.setError("Required Field");
                    }
                    if (showName.getText().length() == 0) {
                        showName.setError("Required Field");
                    } else if (episodeNumber.getText().length() == 0) {
                        episodeNumber.setError("Required Field");
                    }

                } else {
                    //Editing an existing show

                    //Add changes
                    MainActivity.list.get(listItem).setName(showName.getText().toString());
                    MainActivity.list.get(listItem).setNumber(Integer.parseInt(episodeNumber.getText().toString()));
                    MainActivity.list.get(listItem).setUrl(urlText.getText().toString());
                    MainActivity.list.get(listItem).setNotifications(notifyCheckbox.isChecked());
                    MainActivity.list.get(listItem).setTime(time);

                    saveData();

                    if (urlText.getText().length() > 0) {
                        int episode = Integer.parseInt(episodeNumber.getText().toString());
                        String specificUrl = urlText.getText().toString();
                        if(urlText.getText().toString().toLowerCase().contains("anime")) {
                            specificUrl = urlText.getText().toString()  + (episode + 1) + "/";
                        }
                        //Intent stream = new Intent(Intent.ACTION_VIEW, Uri.parse(specificUrl));

                        //Copy URL to Clipboard
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", specificUrl);
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(getApplicationContext(), "URL Copied", Toast.LENGTH_SHORT).show();


                        Intent stream = new Intent(Intent.ACTION_VIEW);
                        stream.setData(Uri.parse(specificUrl));
                        if(isPackageExisted("com.hsv.freeadblockerbrowser")) {
                            stream.setPackage("com.hsv.freeadblockerbrowser");
                        }

                        try {
                            if (stream.resolveActivity(getPackageManager()) != null) {
                                if(isPackageExisted("com.hsv.freeadblockerbrowser")) {
                                    startActivity(stream);
                                } else {
                                    Intent chooser = new Intent (Intent.ACTION_VIEW, Uri.parse(specificUrl));
                                    startActivity(chooser);
                                }
                            }
                            saveData();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Problem Opening URL", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "URL Not Entered", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditorActivity.this);
                builder.setTitle("Finished Conformation");
                if(darkTheme) {
                    builder.setIcon(R.drawable.check_white);
                } else {
                    builder.setIcon(R.drawable.check_black);
                }
                builder.setMessage("Are you done watching \"" + showName.getText() + "\"?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Congrats on finishing \"" + showName.getText() + "\"!", Toast.LENGTH_LONG).show();
                        MainActivity.doneList.add(MainActivity.list.get(listItem));
                        MainActivity.list.remove(listItem);
                        saveDoneData();
                        saveData();
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
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

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(getApplicationContext(), MainActivity.class);
                MainActivity.planList.add(MainActivity.list.get(listItem));
                MainActivity.list.remove(listItem);
                saveData();
                SharedPreferences preferences = getSharedPreferences("Plan List", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(MainActivity.planList);
                editor.putString("Plan List", json);
                editor.apply();
                Toast.makeText(getApplicationContext(), "\""+ showName.getText() + "\" added to watch later", Toast.LENGTH_SHORT).show();
                startActivity(startMain);
            }
        });
        notifyCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (notifyCheckbox.isChecked()) {
                    notificationTime.setVisibility(View.VISIBLE);
                    dayList.setVisibility(View.VISIBLE);
                    timeList.setVisibility(View.VISIBLE);
                    atText.setVisibility(View.VISIBLE);
                } else {
                    notificationTime.setVisibility(View.INVISIBLE);
                    dayList.setVisibility(View.INVISIBLE);
                    timeList.setVisibility(View.INVISIBLE);
                    atText.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    public boolean isPackageExisted(String targetPackage){
        List<ApplicationInfo> packages;
        PackageManager pm;

        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.equals(targetPackage))
                return true;
        }
        return false;
    }

    public void cancelAlarm(Episode show) {
        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("showName", show.getName());
        intent.putExtra("showNumber", show.getNumber());
        intent.putExtra("showUrl", show.getUrl());
        intent.putExtra("showIndex", MainActivity.list.indexOf(show));
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

        calendar.set(Calendar.HOUR_OF_DAY, show.getTime().getHour());
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_WEEK, show.getTime().getDay());

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        intent.putExtra("showName", show.getName());
        intent.putExtra("showNumber", show.getNumber());
        intent.putExtra("showUrl", show.getUrl());
        intent.putExtra("showIndex", MainActivity.list.indexOf(show));
        intent.putExtra("id", show.getId());

        long weeklyInterval = 1000 * 60 * 60 * 24 * 7;
        long diff = now.getTimeInMillis() - calendar.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), show.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (diff > 0) {
            alarmManager.cancel(pendingIntent);
            //calendar.add(Calendar.DAY_OF_YEAR, 1); //Avoid firing when save button is clicked
        } else {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), weeklyInterval, pendingIntent);
            //Toast.makeText(getApplicationContext(), "Item: " + MainActivity.list.indexOf(show) + "\nDay: "+show.getTime().getDay() + " Hour: "+show.getTime().getHour()+"", Toast.LENGTH_LONG).show();
            // alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }



    }

    private void saveData() {
        SharedPreferences preferences = getSharedPreferences("Episode List", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.list);
        editor.putString("List", json);
        editor.apply();
    }

    private void saveDoneData() {
        SharedPreferences preferences = getSharedPreferences("Done List", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.doneList);
        editor.putString("Done List", json);
        editor.apply();
    }

    private ArrayList<String> getHours() {
        String[] hourArray = {"12:00 AM", "1:00 AM", "2:00 AM", "3:00 AM", "4:00 AM", "5:00 AM", "6:00 AM","7:00 AM", "8:00 AM", "9:00 AM","10:00 AM", "11:00 AM", "12:00 PM",
                "1:00 PM", "2:00 PM","3:00 PM", "4:00 PM","5:00 PM", "6:00 PM","7:00 PM", "8:00 PM","9:00 PM", "10:00 PM","11:00 PM"};
        ArrayList<String> hours = new ArrayList<>(Arrays.asList(hourArray));
        return hours;
    }

    private ArrayList<String> getDays() {
        String[] week = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        ArrayList<String> days = new ArrayList<>(Arrays.asList(week));
        return days;
    }

    @Override
    protected void onStop() {
        SharedPreferences preferences = getSharedPreferences("Plan List", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(MainActivity.planList);
        editor.putString("Plan List", json);
        editor.apply();
        saveDoneData();
        saveData();
        super.onStop();
    }
}
