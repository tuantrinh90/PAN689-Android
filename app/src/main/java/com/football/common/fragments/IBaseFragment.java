package com.football.common.fragments;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.application.AppContext;
import com.football.di.AppComponent;

/**
 * Created by dangpp on 2/21/2018.
 */
public interface IBaseFragment {
    AppContext getAppContext();

    AppComponent getAppComponent();

    void bindButterKnife(View view);

    int getTitleId();

    String getTitleString();

    void initToolbar(@NonNull ActionBar supportActionBar);

    void showProgress(boolean show);
}
