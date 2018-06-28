package com.football.customizes.match_up;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchupTextItem extends LinearLayout {

    @BindView(R.id.tvLeft)
    ExtTextView tvLeft;
    @BindView(R.id.tvCenter)
    ExtTextView tvCenter;
    @BindView(R.id.tvRight)
    ExtTextView tvRight;

    public MatchupTextItem(Context context) {
        this(context, null, 0);
    }

    public MatchupTextItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MatchupTextItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.match_up_league_field_item, this);
        ButterKnife.bind(this, view);
    }

    public void setText(String textLeft, String textCenter, String textRight) {
        tvLeft.setText(textLeft);
        tvCenter.setText(textCenter);
        tvRight.setText(textRight);
    }

    public void setTextLeft(String text) {
        tvLeft.setText(text);
    }

    public void setTextCenter(String text) {
        tvCenter.setText(text);
    }

    public void setTextRight(String text) {
        tvRight.setText(text);
    }

}
