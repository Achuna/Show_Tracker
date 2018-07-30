package com.example.achuna.tracker;

import android.app.ProgressDialog;
import android.content.Context;
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

public class DatabaseRewrite extends AsyncTask<Void, Void, ArrayList<Episode>> {

    Context context;
    SQLiteHandler database;
    AsyncResponse delegate = null;

    public interface AsyncResponse {
        void processFinished(ArrayList<Episode> shows);
    }

    public DatabaseRewrite(Context context, AsyncResponse delegate) {
        this.context = context;
        this.delegate = delegate;
        database = new SQLiteHandler(context, null, null, 1);
    }

    @Override
    protected ArrayList<Episode> doInBackground(Void... params) {

        String url = "http://10.0.2.2/Shows/get.php";

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

                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.i("JSON", allShows.get(i).toString());
                }

                if(allShows.size() > 0) database.clearShows();

                return allShows;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Episode> episodes) {
        delegate.processFinished(episodes);
    }
}
