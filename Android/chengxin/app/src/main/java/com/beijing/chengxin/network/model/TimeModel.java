package com.beijing.chengxin.network.model;

/**
 * Created by star on 11/2/2017.
 */

public class TimeModel {
    int year;
    int month;
    int date;
    int day;
    int hours;
    int minutes;
    int seconds;
    int timezoneOffset;

    public String getDateString() {
        return String.format("%4d-%d-%d", year + 1900, month + 1, date);
    }

    public String getDateTimeString() {
        return String.format("%4d-%d-%d %2d:%2d:%2d", year + 1900, month + 1, date, hours, minutes, seconds);
    }

    public String getDateHourMinString() {
        return String.format("%4d-%d-%d %2d:%2d", year + 1900, month + 1, date, hours, minutes);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }
}
