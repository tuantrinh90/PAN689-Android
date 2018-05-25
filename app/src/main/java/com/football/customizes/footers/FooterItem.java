package com.football.customizes.footers;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FooterItem extends LinearLayout {
    @BindView(R.id.llView)
    LinearLayout llView;
    @BindView(R.id.ivIcon)
    ImageView ivIcon;
    @BindView(R.id.tvContent)
    ExtTextView tvContent;

    Drawable icon;

    public FooterItem(Context context) {
        super(context);
        initView(context, null);
    }

    public FooterItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public FooterItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FooterItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.footer_item, this);
        ButterKnife.bind(this, view);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FooterItem);

        // icon
        icon = typedArray.getDrawable(R.styleable.FooterItem_footerItemIcon);
        ivIcon.setImageDrawable(icon);

        // content
        tvContent.setText(typedArray.getString(R.styleable.FooterItem_footerItemContent));

        typedArray.recycle();
    }

    public void setActiveMode(Context context, boolean isActive) {
        // view
        if (isActive) {
            llView.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
        } else {
            llView.setBackground(null);
        }

        // icon
        icon.setColorFilter(ContextCompat.getColor(context, isActive ? R.color.color_blue : R.color.color_white), PorterDuff.Mode.SRC_ATOP);
        ivIcon.setImageDrawable(icon);

        // text
        tvContent.setTextColor(ContextCompat.getColor(context, isActive ? R.color.color_blue : R.color.color_white));
    }
}
