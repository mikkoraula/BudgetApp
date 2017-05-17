package com.example.mikko.budgetapplication;

import java.util.Calendar;

/**
 * Created by Mikko on 13.8.2016.
 */
public class DateHandler {

    private static final int MINS_PER_DAY = 60 * 24;
    private static final long MS_PER_DAY = 1000 * 60 * MINS_PER_DAY;

    private static final int SEC = 1000;
    private static final int MIN = SEC * 60;
    private static final int HOUR = MIN * 60;
    private static final int DAY = HOUR * 24;
    private static final long WEEK = DAY * 7;
    private static final long YEAR = WEEK * 52;

    public static String[] months = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    public static int getMonthFromCurrentMonth(int distanceFromCurrentMonth) {

        int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH) - 1;
        int targetIndex = currentMonthIndex + distanceFromCurrentMonth;

        while (targetIndex < 0) {
            targetIndex += 12;
        }
        while (targetIndex > 11) {
            targetIndex -= 12;
        }

        return targetIndex;
    }

    public static int getYearFromCurrentMonth(int distanceFromCurrentMonth) {
        int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH) - 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        int targetIndex = currentMonthIndex + distanceFromCurrentMonth;

        while (targetIndex < 0) {
            targetIndex += 12;
            currentYear--;
        }
        while (targetIndex > 11) {
            targetIndex -= 12;
            currentYear++;
        }

        return currentYear;
    }

    public static int getYear(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int year = calendar.get(Calendar.YEAR);
        //System.out.println("got a year from milliseconds: " + year);
        return year;
    }

    public static int getMonth(long milliseconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        int month = calendar.get(Calendar.MONTH);
        //System.out.println("got a month from milliseconds: " + month);
        return month;
    }
}
