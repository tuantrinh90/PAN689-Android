package com.football.customizes.progress;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ExtProgress extends ProgressBar {

    private CountDownTimer timer;
    private boolean timerRunning;
    private long currentDuration;

    public ExtProgress(Context context) {
        super(context);
    }

    public ExtProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        currentDuration = progress * 1000;
    }

    public void onDestroyView() {
        stop();
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
                setProgress((int) (currentDuration));
            }

            @Override
            public void onFinish() {
                stop();
            }
        };
        timer.start();
    }

    public void stop() {
        if (!timerRunning) {
            return;
        }

        timerRunning = false;
        timer.cancel();
    }
}
