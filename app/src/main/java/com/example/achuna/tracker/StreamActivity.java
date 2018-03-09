package com.example.achuna.tracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StreamActivity extends AppCompatActivity {

    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final boolean darkTheme = MainActivity.darkTheme;
        String color = MainActivity.appColor;

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
        setContentView(R.layout.activity_stream);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String stream = extras.getString("url");
                    Toast.makeText(getApplicationContext(), stream, Toast.LENGTH_SHORT).show();
                    Intent openUrl = new Intent(Intent.ACTION_VIEW, Uri.parse(stream));
                    try {
                        startActivity(openUrl);
                    } catch (Exception e) {
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        Toast.makeText(getApplicationContext(), "Error Loading URL", Toast.LENGTH_SHORT).show();
                        startActivity(toMain);
                    }
                }
            }
        }, 2000);


        }

    @Override
    protected void onRestart() {
        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(toMain);
        super.onRestart();
    }
}

