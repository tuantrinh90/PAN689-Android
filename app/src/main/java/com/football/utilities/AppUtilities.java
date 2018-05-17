package com.football.utilities;

import java.util.Calendar;

public class AppUtilities {
    /**
     * @return
     */
    public static Calendar getMinCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -120);
        return calendar;
    }

    public static Calendar getMaxCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 120);
        return calendar;
    }
}
