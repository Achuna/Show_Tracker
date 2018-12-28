package com.example.achuna.tracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class DatabaseCheckup extends AsyncTask<Void, Boolean, Boolean> {

    Context context;
    AsyncResponse delegate = null;
    SQLiteHandler database;
    String ip;
    int storage;

    public interface AsyncResponse {
        void processFinished(boolean changes);
    }

    public DatabaseCheckup(Context context, String ip, int storage, AsyncResponse delegate) {
        this.context = context;
        this.ip = ip;
        this.storage = storage;
        this.delegate = delegate;
        database = new SQLiteHandler(context, null, null, 1);
    }



    @Override
    protected Boolean doInBackground(Void... params) {

        boolean results = false;
        String url;
        if(storage == 1) {
            url = "http://achunaofonedu.000webhostapp.com/Shows/get.php";
        } else {
            url = "http://" + ip + "/Shows/get.php";
        }

        ArrayList<Episode> allShows = new ArrayList<>();

        try {
            URL get = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) get.openConnection();
            //Set params for the connection
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("GET");

            InputStream is = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            //Build JSON Response
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            Log.i("JSON", builder.toString());

            //Close byte stream
            is.close();
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(builder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jb = jsonArray.getJSONObject(i);
                allShows.add(new Episode(jb.getString("name"), jb.getInt("number"), jb.getString("url"), (jb.getInt("notification") > 0),
                        new Time(jb.getInt("day"), jb.getInt("hour"), jb.getInt("timeOfDay"), jb.getString("timePreview")), jb.getInt("showId"), jb.getInt("listId")));
            }

            ArrayList<Episode> currentShows = new ArrayList<>();
            Cursor data = database.getData();
            while(data.moveToNext()) {
                currentShows.add(new Episode(data.getString(1), data.getInt(2), data.getString(3), (data.getInt(4) > 0),
                        new Time(data.getInt(5), data.getInt(6), data.getInt(7), data.getString(8)), data.getInt(9), data.getInt(10)));
            }

            if(allShows.size() != currentShows.size()) {
                return false;
            } else {
                ArrayList<String> namesOfCurrent = new ArrayList<>();
                for(int a = 0; a < currentShows.size(); a++) namesOfCurrent.add(currentShows.get(a).getName());

                for(int i = 0; i < allShows.size(); i++) {
                    String name = allShows.get(i).getName();
                    int num = allShows.get(i).getNumber();
                    String showUrl = allShows.get(i).getUrl();

                    int index = namesOfCurrent.indexOf(name);
                    if(index == -1) {
                        return false;
                    } else if(currentShows.get(index).getName().equals(name) && currentShows.get(index).getNumber() == num && currentShows.get(index).getUrl().equals(showUrl)) {
                        results = true;
                    } else {
                        return false;
                    }
                }
            }

            return results;
        } catch (Exception e) {
           // return new ArrayList<Episode>(0);
            results = false;
        }

        return results;
    }

    @Override
    protected void onPostExecute(Boolean changes) {
        delegate.processFinished(changes);
    }
}
