package com.football.utilities;

import android.app.Activity;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.HashMap;

public class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener {
    static final int KEYBOARD_PART = 3;
    static final HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();

    SoftKeyboardToggleListener mCallback;

    final View mRootView;
    boolean previousVisibility;

    private KeyboardUtils(@NonNull Activity act, @NonNull SoftKeyboardToggleListener listener) {
        resetState();
        mCallback = listener;
        mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = mRootView.getRootView().getHeight();
        int heightDiff = screenHeight - (r.bottom - r.top);
        boolean visible = heightDiff > screenHeight / KEYBOARD_PART;
        if (mCallback != null) {
            if (previousVisibility != visible) {
                mCallback.onToggleSoftKeyboard(visible);
            }
        }
        previousVisibility = visible;
    }


    public static void addKeyboardToggleListener(@NonNull Activity act, @NonNull SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);
        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    public static void removeKeyboardToggleListener(@NonNull SoftKeyboardToggleListener listener) {
        if (sListenerMap.containsKey(listener)) {
            KeyboardUtils k = sListenerMap.get(listener);
            k.removeListener();
            sListenerMap.remove(listener);
        }
    }

    public static void removeAllKeyboardToggleListeners() {
        for (SoftKeyboardToggleListener l : sListenerMap.keySet()) {
            sListenerMap.get(l).removeListener();
        }
        sListenerMap.clear();
    }

    void removeListener() {
        resetState();
        mCallback = null;
        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    void resetState() {
        previousVisibility = true;
    }

    public interface SoftKeyboardToggleListener {
        void onToggleSoftKeyboard(boolean isVisible);
    }
}