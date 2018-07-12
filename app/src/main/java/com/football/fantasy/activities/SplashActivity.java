package com.football.fantasy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.bon.share_preferences.AppPreferences;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.fantasy.R;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;


/**
 * Created by HungND on 3/2/18.
 */

public class SplashActivity extends BaseAppCompatActivity {

    @Override
    protected int getContentViewId() {
        return R.layout.splash_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(() -> {
            UserResponse user = AppPreferences.getInstance(this).getObject(Constant.KEY_USER, UserResponse.class);
            startActivity(new Intent(this, user == null ? AccountActivity.class : MainActivity.class));
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

}
