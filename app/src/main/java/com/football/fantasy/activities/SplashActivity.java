package com.football.fantasy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.football.common.activities.AloneFragmentActivity;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.fantasy.fragments.account.signin.SignInFragment;


/**
 * Created by HungND on 3/2/18.
 */

public class SplashActivity extends BaseAppCompatActivity {

    @Override
    protected int getContentViewId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AloneFragmentActivity.with(this)
                .parameters(new Bundle())
                .start(SignInFragment.class);
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
