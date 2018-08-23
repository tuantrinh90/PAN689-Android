package com.football.customizes.textview;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import com.bon.customview.textview.ExtTextView;
import com.football.utilities.AppUtilities;

public class ExtTextViewCountdown extends ExtTextView {

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private long time;
    private long interval = 1;
    private boolean starting;

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
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void start() {
        if (mRunnable == null) {
            mRunnable = () -> {
                starting = true;
                setText(AppUtilities.timeLeft(time));
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

    public void stop() {
        mHandler.removeCallbacks(mRunnable);
        starting = false;
    }

}
