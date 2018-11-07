package com.football.utilities;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bon.share_preferences.AppPreferences;
import com.bon.util.DateTimeUtils;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.football.utilities.Constant.FORMAT_DATE;

public class AppUtilities {
    public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null) {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

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

    public static void displayPlayerPosition(ImageView imageView, Integer position) {
        if (position == null || position == PlayerResponse.POSITION_NONE) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setVisibility(View.VISIBLE);
            switch (position) {
                case PlayerResponse.POSITION_ATTACKER:
                    imageView.setImageResource(R.drawable.ic_player_position_a);
                    break;

                case PlayerResponse.POSITION_MIDFIELDER:
                    imageView.setImageResource(R.drawable.ic_player_position_m);
                    break;

                case PlayerResponse.POSITION_DEFENDER:
                    imageView.setImageResource(R.drawable.ic_player_position_d);
                    break;

                case PlayerResponse.POSITION_GOALKEEPER:
                    imageView.setImageResource(R.drawable.ic_player_position_g);
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
        return df.format(value / oneK) + " k";
    }

    public static String convertNumber(Long value) {
        DecimalFormat df = new DecimalFormat("###,###.##");
        return df.format(value);
    }

    public static String getNameOrMe(Context context, LeagueResponse league) {
        return league.getOwner() ? context.getResources().getString(R.string.me) : league.getUser().getName();
    }

    public static String getNameOrMe(Context context, TeamResponse team) {
        int myId = AppPreferences.getInstance(context).getInt(Constant.KEY_USER_ID);
        return myId == team.getUserId() ? context.getResources().getString(R.string.me) : team.getUser().getName();
    }

    public static boolean isOwner(Context context, int userId) {
        int myId = AppPreferences.getInstance(context).getInt(Constant.KEY_USER_ID);
        return myId == userId;
    }

    public static long getTimestamp(String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(Constant.FORMAT_DATE_TIME_SERVER);
            if (!TextUtils.isEmpty(date)) {
                Date parsedDate = df.parse(date);
                return parsedDate.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean inTime(String setupTime) {
        return !TextUtils.isEmpty(setupTime) && System.currentTimeMillis() < AppUtilities.getTimestamp(setupTime);
    }

    public static boolean isSetupTime(LeagueResponse league) {
        return league.equalsStatus(LeagueResponse.WAITING_FOR_START) &&
                league.equalsGameplay(LeagueResponse.GAMEPLAY_OPTION_TRANSFER) ?
                inTime(league.getTeamSetup()) :
                inTime(league.getDraftTime());
    }

    public static String timeLeft(long totalSecs) {
        long days = totalSecs / (3600 * 24);
        long hours = (totalSecs % (3600 * 24)) / 3600;
        long minutes = (totalSecs % 3600) / 60;

        if (days > 0) {
            return String.format("%dd%02dh", days, hours);
        } else if (hours > 0) {
            return String.format("%02dh%02dm", hours, minutes);
        } else if (minutes > 0) {
            long seconds = totalSecs % 60;
            return String.format("%02dm%02ds", minutes, seconds);
        } else {
            long seconds = totalSecs % 60;
            return String.format("%02ds", seconds);
        }
    }

    public static String timeLeft1(long totalSecs) {
        long days = totalSecs / (3600 * 24);
        long hours = (totalSecs % (3600 * 24)) / 3600;
        long minutes = (totalSecs % 3600) / 60;

        if (days > 0) {
            return String.format("%dd%dh", days, hours);
        } else if (hours > 0) {
            return String.format("%dh%dm", hours, minutes);
        } else if (minutes > 0) {
            long seconds = totalSecs % 60;
            return String.format("%dm%ds", minutes, seconds);
        } else {
            long seconds = totalSecs % 60;
            return String.format("%ds", seconds);
        }
    }

    public static String timeLeft2(long totalSecs) {
        long days = totalSecs / (3600 * 24);
        long hours = (totalSecs % (3600 * 24)) / 3600;
        long minutes = (totalSecs % 3600) / 60;

        if (days > 0) {
            return String.format("%dd:%02dh", days, hours);
        } else {
            long seconds = totalSecs % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    public static String getDayOfWeek(String date) {
        Calendar calendar = DateTimeUtils.convertStringToCalendar(date, Constant.FORMAT_DATE_TIME_SERVER);
        return DateTimeUtils.convertCalendarToString(calendar, Constant.FORMAT_DAY_OF_WEEK);
    }

    public static String getDateFormatted(String unformattedDate) {
        return getTimeFormatted(unformattedDate, FORMAT_DATE);
    }

    public static String getTimeFormatted(String unformattedTime) {
        return getTimeFormatted(unformattedTime, Constant.FORMAT_DATE_TIME);
    }

    /**
     * chuyển đổi time server sang định dạng time mong muốn
     */
    public static String getTimeFormatted(String unformattedTime, String format) {
        return getTimeFormatted(unformattedTime, Constant.FORMAT_DATE_TIME_SERVER, format);
    }

    public static String getTimeFormatted(String unformattedTime, String target, String format) {
        return DateTimeUtils.convertCalendarToString(DateTimeUtils.convertStringToCalendar(unformattedTime, target), format);
    }

    public static int getDraftEstimate(int numberOfUser, int timePerDraftPick) {
        int value = numberOfUser * 18 * timePerDraftPick / 60 + 1;
        if (value % 15 == 0) {
            return value;
        } else {
            value += 15 - value % 15;
            return value;
        }
    }

    public static CharSequence getRelativeTimeSpanString(String time) {
        Calendar calendar = DateTimeUtils.convertStringToCalendar(time, Constant.FORMAT_DATE_TIME_SERVER);
        return calendar != null ?
                DateUtils.getRelativeTimeSpanString(
                        calendar.getTimeInMillis(),
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS)
                : "";
    }
}
