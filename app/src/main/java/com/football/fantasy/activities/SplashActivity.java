package com.football.fantasy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.bon.share_preferences.AppPreferences;
import com.football.fantasy.R;
import com.football.utilities.Constant;


/**
 * Created by HungND on 3/2/18.
 */

public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Handler().postDelayed(() -> {
            String token = AppPreferences.getInstance(this).getString(Constant.KEY_TOKEN);
            Intent intent = new Intent(this, TextUtils.isEmpty(token) ? AccountActivity.class : MainActivity.class);

            startActivity(intent);
            finish();
        }, 1000);
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return null;
    }

    @Override
    public AppBarLayout getAppBarLayout() {
        return null;
    }

    @Override
    public Toolbar getToolBar() {
        return null;
    }

    private String getDeviceResolution() {
        int density = getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }
}
