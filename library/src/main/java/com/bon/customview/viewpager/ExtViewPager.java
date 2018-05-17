package com.bon.customview.viewpager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ExtViewPager extends ViewPager {
    private float initialXValue;
    private ExtViewPagerSwipeDirection direction = ExtViewPagerSwipeDirection.ALL;

    public ExtViewPager(@NonNull Context context) {
        super(context);
    }

    public ExtViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (IsSwipeAllowed(event)) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (IsSwipeAllowed(event)) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private boolean IsSwipeAllowed(MotionEvent event) {
        if (this.direction == ExtViewPagerSwipeDirection.ALL) return true;

        if (direction == ExtViewPagerSwipeDirection.NONE)//disable any swipe
            return false;

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            initialXValue = event.getX();
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - initialXValue;
                if (diffX > 0 && direction == ExtViewPagerSwipeDirection.RIGHT) {
                    // swipe from left to right detected
                    return false;
                } else if (diffX < 0 && direction == ExtViewPagerSwipeDirection.LEFT) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return true;
    }

    /**
     * @param direction
     */
    public ExtViewPager setAllowedSwipeDirection(ExtViewPagerSwipeDirection direction) {
        this.direction = direction;
        return this;
    }
}