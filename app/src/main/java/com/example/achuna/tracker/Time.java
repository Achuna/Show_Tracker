package com.example.achuna.tracker;

/**
 * Created by Achuna on 2/25/2018.
 */

public class Time {
    int day, hour, timeOfDay;
    String timePreview;

    public Time(int day, int hour, int timeOfDay, String timePreview) {
        this.day = day;
        this.hour = hour;
        this.timeOfDay = timeOfDay;
        this.timePreview = timePreview;
    }

    public String getTimePreview() {
        return timePreview;
    }

    public void setTimePreview(String timePreview) {
        this.timePreview = timePreview;
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

    public String convertToDay(int a) {
        String day = "";
        if (a == 1) {
            day = "Sundays";
        } else if (a == 2) {
            day = "Mondays";
        } else if (a == 3) {
            day = "Tuesdays";
        } else if (a == 4) {
            day = "Wednesdays";
        }else if (a == 5) {
            day = "Thursdays";
        }else if (a == 6) {
            day = "Fridays";
        }else if (a == 7) {
            day = "Saturdays";
        }
        return day;
    }

    public String convertToHour(int a) {

        if(a == 0) {
            return "12";
        } else if(a > 12) {
            a = (a-12);
        }
        return a +"";
    }

    public String convertTimeOfDay(int a) {
        if (a == 0) {
            return "AM";
        } else {
            return "PM";
        }
    }

    @Override
    public String toString() {
        return convertToDay(day) + " at " + convertToHour(hour) + " " +convertTimeOfDay(timeOfDay);
    }
}
