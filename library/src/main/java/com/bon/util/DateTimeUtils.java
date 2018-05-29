package com.bon.util;

import android.annotation.SuppressLint;

import com.bon.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dang on 10/14/2015.
 */
public class DateTimeUtils {
    private static final String TAG = DateTimeUtils.class.getSimpleName();

    /**
     * calendar no time
     *
     * @param year
     * @param month
     * @param dayOfMonth
     * @return
     */
    public static Calendar getCalendarNoTime(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        return calendar;
    }

    /**
     * @param year
     * @param month
     * @param dayOfMonth
     * @param hour
     * @param minute
     * @return
     */
    public static Calendar getCalendarTime(int year, int month, int dayOfMonth, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, dayOfMonth, hour, minute, 0);
        return calendar;
    }

    /**
     * convert millisecond to calendar
     *
     * @param number
     * @return
     */
    public static Calendar convertTimeStampToDateTime(float number) {
        try {
            if (number <= 0) return null;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis((long) number);
            return calendar;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return null;
    }

    /**
     * convert calendar to string
     *
     * @param calendar
     * @param pattern
     * @return
     */
    public static String convertCalendarToString(Calendar calendar, String pattern) {
        try {
            if (calendar == null) return "";
            return convertDateToString(calendar.getTime(), pattern);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return "";
    }

    /**
     * @param date
     * @param pattern
     * @return
     */
    public static String convertDateToString(Date date, String pattern) {
        try {
            if (date == null) return "";

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, Locale.US);
            return simpleDateFormat.format(date);
        } catch (Exception ex) {
            Logger.e(TAG, ex);
        }

        return "";
    }

    /**
     * @param year
     * @return
     */
    public static boolean isLeapYear(int year) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

        return false;
    }
}
