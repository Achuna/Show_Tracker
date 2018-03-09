package com.example.achuna.tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    Switch darkThemeSwitch;
    EditText settingListName, settingMainScreenHeader;
    ListView colorSwitcher;
    TextView colorText;
    Button settingSaveBtn;


    boolean darkTheme = false;
    String listName = "Episode List", header = "Tracker";
    String color = "";
    int[] colors = {R.color.bluePrimary, R.color.yellowPrimary, R.color.orangePrimary, R.color.redPrimary, R.color.greenPrimary, R.color.purplePrimary};
    String[] colorNames = {"Blue","Yellow", "Orange", "Red", "Green", "Purple"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences theme = getSharedPreferences("Theme", MODE_PRIVATE);
        darkTheme = theme.getBoolean("Dark Theme", false);
        SharedPreferences getColor = getSharedPreferences("Color", MODE_PRIVATE);
        color = getColor.getString("color", color);
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
        setContentView(R.layout.activity_settings);

        //Initialize Views
        darkThemeSwitch = findViewById(R.id.themeSwitch);
        settingListName = findViewById(R.id.settingsListNameEdit);
        settingMainScreenHeader = findViewById(R.id.settingsMainScreenHeader);
        colorSwitcher = findViewById(R.id.colorSwitcher);
        settingSaveBtn = findViewById(R.id.settingsSaveBtn);
        colorText = findViewById(R.id.colorText);

        darkThemeSwitch.setChecked(darkTheme);


        SharedPreferences titles = getSharedPreferences("Settings", MODE_PRIVATE);

        settingListName.setText(titles.getString("List Title", null));
        settingMainScreenHeader.setText(titles.getString("Header", null));


        colorText.setText("Current Color: " + color);

        ColorSpinnerAdapter adapter = new ColorSpinnerAdapter(getApplicationContext(), colors, colorNames, darkTheme);
        colorSwitcher.setAdapter(adapter);

        colorSwitcher.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences colorEdit = getSharedPreferences("Color", MODE_PRIVATE);
                SharedPreferences.Editor editor = colorEdit.edit();
                editor.putString("color", colorNames[i]);
                editor.apply();
                SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = settings.edit();
                editor1.putString("Header", settingMainScreenHeader.getText().toString());
                editor1.putString("List Title", settingListName.getText().toString());
                editor1.apply();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                darkTheme = b;

                SharedPreferences settings = getSharedPreferences("Theme", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("Dark Theme", darkTheme);
                editor.apply();

                saveSettingData();

                MainActivity.listTitle =  settingListName.getText().toString();
                MainActivity.toolbarTitle =  settingMainScreenHeader.getText().toString();

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


        settingSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Settings Saved", Toast.LENGTH_SHORT).show();
                Intent goToMain = new Intent(getApplicationContext(), MainActivity.class);
                SharedPreferences settings = getSharedPreferences("Setting", MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Header", settingMainScreenHeader.getText().toString());
                editor.putString("List Title", settingListName.getText().toString());
                editor.apply();

                SharedPreferences mainColor = getSharedPreferences("App Color", MODE_PRIVATE);
                SharedPreferences.Editor editor1 = mainColor.edit();
                editor1.putString("main color", color);
                editor1.apply();

                SharedPreferences mainTheme = getSharedPreferences("App Theme", MODE_PRIVATE);
                SharedPreferences.Editor editor2 = mainTheme.edit();
                editor2.putBoolean("main theme", darkTheme);
                editor2.apply();

                SharedPreferences titles = getSharedPreferences("Titles", MODE_PRIVATE);
                SharedPreferences.Editor editor3 = titles.edit();
                editor3.putString("header", settingMainScreenHeader.getText().toString());
                editor3.putString("list title", settingListName.getText().toString());
                editor3.apply();

                startActivity(goToMain);
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(getApplicationContext(), "Settings Not Saved", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        saveSettingData();
        super.onStop();
    }

    private void saveSettingData() {
        SharedPreferences settings = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("Header", settingMainScreenHeader.getText().toString());
        editor.putString("List Title", settingListName.getText().toString());
        editor.apply();
        SharedPreferences colorPref = getSharedPreferences("Color", MODE_PRIVATE);
        SharedPreferences.Editor colorEditor = colorPref.edit();
        colorEditor.putString("color", color);
        colorEditor.apply();
    }


}
