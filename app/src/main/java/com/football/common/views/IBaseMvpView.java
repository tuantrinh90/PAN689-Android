package com.football.common.views;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;

public interface IBaseMvpView extends MvpView, LifecycleProvider<FragmentEvent> {
    void showLoading(boolean isLoading);
}
