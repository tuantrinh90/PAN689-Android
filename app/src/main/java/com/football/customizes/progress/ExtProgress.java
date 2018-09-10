package com.football.customizes.progress;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class ExtProgress extends ProgressBar {

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private boolean starting;

    public ExtProgress(Context context) {
        super(context);
    }

    public ExtProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExtProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void start() {
        if (mRunnable == null) {
            mRunnable = () -> {
                starting = true;
                setProgress(getProgress() - 1);
                mHandler.postDelayed(mRunnable, 1000);
                if (getProgress() <= 0) {
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
