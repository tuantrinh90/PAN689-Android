package com.football.customizes.images;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.image.ImageLoaderUtils;
import com.football.fantasy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class CircleImageViewApp extends LinearLayout {
    @BindView(R.id.ivImage)
    CircleImageView ivImage;

    public CircleImageViewApp(Context context) {
        super(context);
        initView(context);
    }

    public CircleImageViewApp(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CircleImageViewApp(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleImageViewApp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.circle_image_view, this);
        ButterKnife.bind(this, view);
    }

    public CircleImageViewApp setImageUri(String uri) {
        ImageLoaderUtils.displayImage(uri, ivImage);
        return this;
    }

    public CircleImageView getImageView() {
        return ivImage;
    }
}
