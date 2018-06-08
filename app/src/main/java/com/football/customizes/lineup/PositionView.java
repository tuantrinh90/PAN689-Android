package com.football.customizes.lineup;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.bon.customview.textview.ExtTextView;

public class PositionView extends ExtTextView {

    public PositionView(Context context) {
        this(context, null, 0);
    }

    public PositionView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PositionView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }


}
