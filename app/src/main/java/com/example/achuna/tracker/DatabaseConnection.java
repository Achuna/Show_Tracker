package com.example.achuna.tracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

public class DatabaseConnection extends AsyncTask<String, Void, ArrayList<Episode>> {

    Context context;
    SQLiteHandler database;

    public DatabaseConnection(Context context) {
        this.context = context;
        database = new SQLiteHandler(context, null, null, 1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected ArrayList<Episode> doInBackground(String... params) {

        String getUrl = "http://10.0.2.2";
        String addUrl = "";


        if(params[0].equals("rewrite")) {
            database.clearShows();

        } else {

        }


        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<Episode> episodes) {
        super.onPostExecute(episodes);
    }
}
