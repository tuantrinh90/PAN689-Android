package com.football.common.views;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IBaseMvpView extends MvpView {
    void showLoading(boolean isLoading);
}
