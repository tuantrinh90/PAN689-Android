package com.football.customizes.textview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.bon.customview.textview.ExtTextView;
import com.football.utilities.AppUtilities;

import java8.util.function.Consumer;

public class ExtTextViewCountdown extends ExtTextView {

    private static final String TAG = "ExtTextViewCountdown";

    public static final int FORMAT_TEXT_HOURS = 0;
    public static final int FORMAT_NUMBER_HOURS = 1;
    public static final int FORMAT_NUMBER_SECONDS_ONLY = 3;

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private long time; // seconds
    private long interval = 1;
    private boolean starting;
    private int formatType = FORMAT_TEXT_HOURS; // FORMAT_TEXT_HOURS: 2h30m, FORMAT_NUMBER_HOURS: 02:30:30

    private Consumer<Void> timeoutCallback;

    public ExtTextViewCountdown(Context context) {
        super(context, null, 0);
    }

    public ExtTextViewCountdown(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public ExtTextViewCountdown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mRunnable = () -> {
            starting = true;
            setText();
            time -= interval;
            if (time < 0) {
                if (timeoutCallback != null) {
                    timeoutCallback.accept(null);
                }
                stop();
            }
            mHandler.postDelayed(mRunnable, 1000);
        };
    }

    public void onDestroyView() {
        stop();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    public void setTimeoutCallback(Consumer<Void> timeoutCallback) {
        this.timeoutCallback = timeoutCallback;
    }

    public void start() {
        if (!starting) {
            mHandler.removeCallbacks(mRunnable);
            init();
            setText();
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    public void reset() {
        starting = false;
        mHandler.removeCallbacks(mRunnable);
        start();
    }

    private void setText() {
        switch (formatType) {
            case FORMAT_TEXT_HOURS:
                setText(AppUtilities.timeLeft(time));
                break;

            case FORMAT_NUMBER_HOURS:
                setText(AppUtilities.timeLeft2(time));
                break;

            case FORMAT_NUMBER_SECONDS_ONLY:
                setText(String.valueOf(time));
                break;
        }
    }

    public void stop() {
        timeoutCallback = null;
        mHandler.removeCallbacks(mRunnable);
        starting = false;
    }

    public boolean isRunning() {
        return time > 0;
    }
}
