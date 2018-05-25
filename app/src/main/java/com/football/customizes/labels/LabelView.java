package com.football.customizes.labels;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class LabelView extends LinearLayout {
    @BindView(R.id.tvLabel)
    ExtTextView tvLabel;
    @BindView(R.id.tvRequired)
    ExtTextView tvRequired;
    @BindView(R.id.ivInfo)
    ImageView ivInfo;

    Consumer<Void> infoConsumer;

    public LabelView(Context context) {
        super(context);
        initView(context, null);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LabelView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.label_view, this);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LabelView, 0, 0);

        // text all caps
        boolean isTextAllCaps = typedArray.getBoolean(R.styleable.LabelView_android_textAllCaps, true);

        // info
        String label = typedArray.getString(R.styleable.LabelView_labelViewLabel) + "";
        tvLabel.setText(isTextAllCaps ? label.toUpperCase() : label);
        tvRequired.setVisibility(typedArray.getBoolean(R.styleable.LabelView_labelViewRequired, false) ? VISIBLE : GONE);
        ivInfo.setVisibility(typedArray.getBoolean(R.styleable.LabelView_labelViewInfo, false) ? VISIBLE : GONE);

        // click
        RxView.clicks(ivInfo).subscribe(o -> Optional.from(infoConsumer).doIfPresent(i -> i.accept(null)));

        // recycle
        typedArray.recycle();
    }

    public LabelView setInfoConsumer(Consumer<Void> infoConsumer) {
        this.infoConsumer = infoConsumer;
        return this;
    }
}
