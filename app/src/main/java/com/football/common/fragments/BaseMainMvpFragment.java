package com.football.common.fragments;

import android.app.Activity;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.fantasy.activities.MainActivity;

public abstract class BaseMainMvpFragment<V extends IBaseMvpView, P extends IBaseDataPresenter<V>> extends BaseMvpFragment<V, P> {
    protected MainActivity mMainActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mMainActivity = (MainActivity) activity;
        }
    }
}
