package com.football.customizes.textview;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;

import com.bon.customview.textview.ExtTextView;
import com.football.utilities.AppUtilities;

import java8.util.function.Consumer;

public class ExtTextViewCountdown extends ExtTextView {

    private static final String TAG = "ExtTextViewCountdown";

    public static final int FORMAT_TEXT_HOURS = 0;
    public static final int FORMAT_NUMBER_HOURS = 1;
    public static final int FORMAT_NUMBER_SECONDS_ONLY = 3;

    private int formatType = FORMAT_TEXT_HOURS; // FORMAT_TEXT_HOURS: 2h30m, FORMAT_NUMBER_HOURS: 02:30:30

    private Consumer<Void> timeoutCallback;
    private CountDownTimer timer;
    private boolean timerRunning;
    private long currentDuration;

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
    }

    public void onDestroyView() {
        timeoutCallback = null;
        stop();
    }

    public void setTime(long time) {
        this.currentDuration = time * 1000;
        setText();
    }

    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    public void setTimeoutCallback(Consumer<Void> timeoutCallback) {
        this.timeoutCallback = timeoutCallback;
    }

    public void start() {
        if (timerRunning) {
            return;
        }

        timerRunning = true;
        timer = new CountDownTimer(currentDuration, 100) {
            @Override
            public void onTick(long millis) {
                currentDuration = millis;
                setText();
            }

            @Override
            public void onFinish() {
                stop();
                if (timeoutCallback != null) {
                    timeoutCallback.accept(null);
                }

            }
        };
        timer.start();
    }

    private void setText() {
        switch (formatType) {
            case FORMAT_TEXT_HOURS:
                setText(AppUtilities.timeLeft(currentDuration / 1000));
                break;

            case FORMAT_NUMBER_HOURS:
                setText(AppUtilities.timeLeft2(currentDuration / 1000));
                break;

            case FORMAT_NUMBER_SECONDS_ONLY:
                setText(String.valueOf(currentDuration / 1000));
                break;
        }
    }

    public void stop() {
        if (!timerRunning) {
            return;
        }

        timerRunning = false;
        timer.cancel();
    }

    public boolean isRunning() {
        return timerRunning;
    }
}
