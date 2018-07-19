package com.football.utilities;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bon.util.DateTimeUtils;
import com.football.application.AppContext;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public static void displayPlayerPosition(TextView textView, Integer position, String value) {
        if (position == null || TextUtils.isEmpty(value) || position == PlayerResponse.POSITION_NONE) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(value);
            switch (position) {
                case PlayerResponse.POSITION_ATTACKER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_a);
                    break;

                case PlayerResponse.POSITION_MIDFIELDER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_m);
                    break;

                case PlayerResponse.POSITION_DEFENDER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_d);
                    break;

                case PlayerResponse.POSITION_GOALKEEPER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_g);
                    break;
            }
        }
    }

    public static String getMoney(Long value) {
        float oneMio = 1000000f;
        float oneK = 1000f;
        DecimalFormat df = new DecimalFormat("###,###.##");
        if (Math.abs(value) >= oneMio) {
            return df.format(value / oneMio) + " mio";
        }
        return df.format(value / oneK) + "k";
    }

    public static String convertNumber(Long value) {
        DecimalFormat df = new DecimalFormat("###,###.##");
        return df.format(value);
    }

    public static String getNameOrMe(Context context, LeagueResponse league) {
        return league.getOwner() ? context.getResources().getString(R.string.me) : league.getUser().getName();
    }

    public static String getNameOrMe(Context context, TeamResponse team) {
        int myId = ((AppContext) context.getApplicationContext()).getMyId();
        return myId == team.getUserId() ? context.getResources().getString(R.string.me) : team.getUser().getName();
    }

    public static boolean isOwner(Context context, int userId) {
        int myId = ((AppContext) context.getApplicationContext()).getMyId();
        return myId == userId;
    }

    public static long getTimestamp(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(Constant.FORMAT_DATE_TIME_SERVER);
            Date parsedDate = df.parse(date);
            return parsedDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean isSetupTime(String setupTime) {
        return System.currentTimeMillis() < AppUtilities.getTimestamp(setupTime);
    }

    public static boolean isStartLeagueEnable(LeagueResponse league) {
        return league.getOwner()
                && !league.getTeamSetup().equals(league.getStartAt())
                && !AppUtilities.isSetupTime(league.getTeamSetup())
                && league.getNumberOfUser() - league.getCurrentNumberOfUser() <= 1
                && league.getStatus() == LeagueResponse.WAITING_FOR_START;
    }

    public static String timeLeft(long totalSecs) {
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        return String.format("%02dh %02d", hours, minutes);
    }

    public static String getDayOfWeek(String date) {
        Calendar calendar = DateTimeUtils.convertStringToCalendar(date, Constant.FORMAT_DATE_TIME_SERVER);
        return DateTimeUtils.convertCalendarToString(calendar, Constant.FORMAT_DAY_OF_WEEK);
    }

    public static String getDate(String date) {
        Calendar calendar = DateTimeUtils.convertStringToCalendar(date, Constant.FORMAT_DATE_TIME_SERVER);
        return DateTimeUtils.convertCalendarToString(calendar, Constant.FORMAT_DATE);
    }

    public static String getDate(String date, String format) {
        Calendar calendar = DateTimeUtils.convertStringToCalendar(date, format);
        return DateTimeUtils.convertCalendarToString(calendar, Constant.FORMAT_DATE);
    }
}
