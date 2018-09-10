package com.football.customizes.textview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.bon.customview.textview.ExtTextView;
import com.football.utilities.AppUtilities;

public class ExtTextViewCountdown extends ExtTextView {

    public static final int FORMAT_TEXT_HOURS = 0;
    public static final int FORMAT_NUMBER_HOURS = 1;

    private boolean isAttached;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private long time;
    private long interval = 1;
    private boolean starting;
    private int formatType = FORMAT_TEXT_HOURS; // FORMAT_TEXT_HOURS: 2h30m, FORMAT_NUMBER_HOURS: 02:30:30

    public ExtTextViewCountdown(Context context) {
        super(context);
    }

    public ExtTextViewCountdown(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtTextViewCountdown(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        if (starting && time <= 0) {
            setText();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setFormatType(int formatType) {
        this.formatType = formatType;
    }

    public void start() {
        if (mRunnable == null) {
            mRunnable = () -> {
                starting = true;
                if (isAttached) {
                    setText();
                }
                mHandler.postDelayed(mRunnable, 1000);
                time -= interval;
                if (time < 0) {
                    stop();
                }
            };
        }

        if (!starting) {
            mHandler.postDelayed(mRunnable, 1000);
        }
    }

    private void setText() {
        setText(formatType == FORMAT_TEXT_HOURS ? AppUtilities.timeLeft(time) : AppUtilities.timeLeft2(time));
    }

    public void stop() {
        mHandler.removeCallbacks(mRunnable);
        starting = false;
    }

    public boolean isRunning() {
        return time > 0;
    }
}
