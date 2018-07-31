package com.example.achuna.tracker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

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

import javax.net.ssl.HttpsURLConnection;

public class DatabaseBackup extends AsyncTask<ArrayList<DataObject>, Void, Boolean> {

    interface AsyncResponse {
        void processFinished(boolean result);
    }

    Context context;
    AsyncResponse delegate;
    String ip;
    int storage;


    public DatabaseBackup(Context context, String ip, int storage, AsyncResponse response) {
        this.context = context;
        this.ip = ip;
        this.storage = storage;
        this.delegate = response;
    }



    @Override
    protected Boolean doInBackground(ArrayList<DataObject>... params) {

        String url;
        if(storage == 1) {
            url = "http://achunaofonedu.000webhostapp.com/Shows/update.php";
        } else {
            url = "http://" + ip + "/Shows/update.php";
        }

        ArrayList<DataObject> shows = params[0];

        Gson gson = new Gson();
        String json = gson.toJson(shows);

        Log.i("JSON", json);

        try {
            URL con = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) con.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
            String data = URLEncoder.encode("array", "UTF-8") + "=" + URLEncoder.encode(json, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            os.close();


            InputStream is = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line = "";
            String response = "";
            while ((line = bufferedReader.readLine()) != null) {
                response += line;
            }

            Log.i("JSON", response);

            return response.contains("Success");


        } catch (Exception e) {
            return false;
            //e.printStackTrace();
        }

    }

    @Override
    protected void onPostExecute(Boolean result) {
        delegate.processFinished(result);
    }



}
