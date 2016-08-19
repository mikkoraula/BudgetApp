package com.example.mikko.budgetapplication;

import java.util.Calendar;

/**
 * Created by Mikko on 13.8.2016.
 */
public class DateHandler {

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

    public static String getMonthFromCurrentMonth(int distanceFromCurrentMonth) {

        int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH) - 1;
        int targetIndex = currentMonthIndex + distanceFromCurrentMonth;

        while (targetIndex < 0) {
            targetIndex += 12;
        }
        while (targetIndex > 11) {
            targetIndex -= 12;
        }

        return months[targetIndex];
    }

    public static int getYearFromCurrentMonth(int distanceFromCurrentMonth) {
        int currentMonthIndex = Calendar.getInstance().get(Calendar.MONTH) - 1;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR) - 1;
        System.out.println(Calendar.getInstance().get(Calendar.YEAR));
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
}
