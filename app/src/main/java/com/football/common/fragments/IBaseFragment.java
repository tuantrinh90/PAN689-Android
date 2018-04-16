package com.football.common.fragments;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.football.application.AppContext;
import com.football.di.AppComponent;

/**
 * Created by dangpp on 2/21/2018.
 */
public interface IBaseFragment {
    AppContext getAppContext();

    AppComponent getAppComponent();

    int getTitleId();

    void initToolbar(@NonNull ActionBar supportActionBar);

    void showProgress(boolean show);
}
