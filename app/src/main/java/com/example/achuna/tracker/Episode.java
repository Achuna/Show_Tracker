package com.example.achuna.tracker;

/**
 * Created by Achuna on 2/20/2018.
 */

public class Episode {

    private String name, url;
    private int number;
    boolean notifications;
    private Time time;
    private int id;
    private int listId; //1: Main list, 2: Plan to Watch, 3: Done

    public Episode(String name, int number, String url, boolean notifications, Time time, int id, int listId) {
        this.name = name;
        this.number = number;
        this.url = url;
        this.notifications = notifications;
        this.time = time;
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

    public boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
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

    @Override
    public String toString() {
        return "Episode{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", number=" + number +
                ", notifications=" + notifications +
                ", time=" + time +
                '}';
    }

}

