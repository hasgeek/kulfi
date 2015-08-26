package com.hasgeek.zalebi.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by heisenberg on 27/06/15.
 */
public class DateTimeUtils {
    public static String displayableTime(String startTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return simpleDateFormat.format(date);
    }

    public static String getDuration(String startTime, String endTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String duration = "";
        try {
            long startTimeInMs = dateFormat.parse(startTime).getTime();
            long endTimeInMs = dateFormat.parse(endTime).getTime();
            duration = String.format("%d min", TimeUnit.MILLISECONDS.toMinutes(endTimeInMs-startTimeInMs));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return duration;
    }

    public static String displayableTimeInterval(String startTime, String endTime) {
        return displayableTime(startTime) + " - " + displayableTime(endTime);
    }

    public static String displayableDate(String startTime) {
        return displayableDate(startTime, false);
    }

    public static String displayableDate(String startTime, boolean shortMonth){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat;
        if(shortMonth){
            simpleDateFormat = new SimpleDateFormat("dd MMM");
        }else {
            simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        }

        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        return simpleDateFormat.format(date);
    }
}
