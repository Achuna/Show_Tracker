package com.example.achuna.tracker;

/**
 * This is used to make JSON Formatted Strings of class 'Episode' easier
 */
public class DataObject {

    private String name, url;
    private int number;
    private int notifications;
    private int day, hour, timeOfDay;
    private String timePreview;
    private int id;
    private int listId;

    public DataObject(String name, int number, String url, int notifications, int day, int hour, int timeOfDay, String timePreview, int id, int listId) {
        this.name = name;
        this.url = url;
        this.number = number;
        this.notifications = notifications;
        this.day = day;
        this.hour = hour;
        this.timeOfDay = timeOfDay;
        this.timePreview = timePreview;
        this.id = id;
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNotifications() {
        return notifications;
    }

    public void setNotifications(int notifications) {
        this.notifications = notifications;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(int timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getTimePreview() {
        return timePreview;
    }

    public void setTimePreview(String timePreview) {
        this.timePreview = timePreview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }
}
