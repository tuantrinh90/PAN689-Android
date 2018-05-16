package com.bon.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.bon.logger.Logger;

public class ViewUtils {
    private static final String TAG = ViewUtils.class.getSimpleName();

    // alpha
    private static AlphaAnimation alpha = null;

    /**
     * invalidate view
     *
     * @param view
     */
    public static void invalidate(View view) {
        try {
            view.requestLayout();
            view.postInvalidate();
            view.refreshDrawableState();
            view.forceLayout();

            // If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    invalidate(innerView);
                }
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * set opacity for view
     *
     * @param view
     */
    public static void setOpacity(View view) {
        try {
            if (alpha != null) alpha.cancel();

            if (view.isEnabled()) {
                alpha = new AlphaAnimation(1.0F, 1.0F);
                // Make animation instant
                alpha.setDuration(0);
                // Tell it to persist after the
                alpha.setFillAfter(true);
                // animation
                view.startAnimation(alpha);
            } else {
                alpha = new AlphaAnimation(0.5F, 0.5F);
                // Make animation
                alpha.setDuration(System.currentTimeMillis());
                // Tell it to persist after the
                alpha.setFillAfter(false);
                // animation
                view.startAnimation(alpha);
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    /**
     * add view tree observer
     *
     * @param view
     * @param onGlobalLayoutListener
     */
    public static void attachViewTreeObserver(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    /**
     * remove view tree observer
     *
     * @param view
     * @param onGlobalLayoutListener
     */
    public static void detachViewTreeObserver(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
        }
    }

    /**
     * scale layout for rate
     *
     * @param view
     * @param scale
     */
    public static void autoLayoutScale(View view, float scale) {
        try {
            // height, width
            int width = view.getWidth();

            // image height for scale view
            int height = (int) (width / scale);

            // set layout param
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            view.setLayoutParams(layoutParams);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }
}
