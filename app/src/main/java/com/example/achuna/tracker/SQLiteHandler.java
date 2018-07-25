package com.example.achuna.tracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Shows";
    private static final String TABLE_NAME = "show_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_NOTIFICATION = "notification";
    private static final String COLUMN_DAY = "day";
    private static final String COLUMN_HOUR = "hour";
    private static final String COLUMN_TIMEOFDAY = "timeOfDay";
    private static final String COLUMN_TIMEPREVIEW = "timePreview";
    private static final String COLUMN_SHOWID = "showId";
    private static final String COLUMN_LISTID = "listID";


    public SQLiteHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " VARCHAR(20) NOT NULL," +
                COLUMN_NUMBER + " INTEGER NOT NULL," +
                COLUMN_URL + " TEXT," +
                COLUMN_NOTIFICATION + " BOOLEAN NOT NULL," +
                COLUMN_DAY + " INTEGER," +
                COLUMN_HOUR + " INTEGER," +
                COLUMN_TIMEOFDAY + " INTEGER," +
                COLUMN_TIMEPREVIEW + " VARCHAR(20)," +
                COLUMN_SHOWID + " INTEGER," +
                COLUMN_LISTID + " INTEGER NOT NULL);";

        db.execSQL(query);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);
        onCreate(db);
    }

    public void addShow(Episode episode) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, episode.getName());
        values.put(COLUMN_NUMBER, episode.getNumber());
        values.put(COLUMN_URL, episode.getUrl());
        values.put(COLUMN_NOTIFICATION, episode.getNotifications());
        values.put(COLUMN_DAY, episode.getTime().getDay());
        values.put(COLUMN_HOUR, episode.getTime().getHour());
        values.put(COLUMN_TIMEOFDAY, episode.getTime().getTimeOfDay());
        values.put(COLUMN_TIMEPREVIEW, episode.getTime().getTimePreview());
        values.put(COLUMN_SHOWID, episode.getId());
        values.put(COLUMN_LISTID, episode.getListId());

        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    public void deleteShow(String showName) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + "=\"" + showName + "\"";
        db.execSQL(query);
    }

    public void updateShow(Episode episode) {
        SQLiteDatabase database = getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_NAME + "=\"" + episode.getName() + "\" WHERE " +COLUMN_SHOWID + "=\"" + episode.getId() +"\" +" +
                "AND " + COLUMN_NUMBER + "=\"" + episode.getNumber() + "\"" +
                "AND " + COLUMN_URL + "=\"" + episode.getUrl() + "\""+
                "AND " + COLUMN_NOTIFICATION + "=\"" + episode.getNotifications() + "\""+
                "AND " + COLUMN_DAY + "=\"" + episode.getTime().getDay() + "\""+
                "AND " + COLUMN_HOUR + "=\"" + episode.getTime().getHour() + "\""+
                "AND " + COLUMN_TIMEOFDAY + "=\"" + episode.getTime().getTimeOfDay() + "\""+
                "AND " + COLUMN_TIMEPREVIEW + "=\"" + episode.getTime().getTimePreview() + "\"";
        database.execSQL(query);
    }

    public void clearShows() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DELETE FROM " + TABLE_NAME);
    }

    public Cursor getData() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
