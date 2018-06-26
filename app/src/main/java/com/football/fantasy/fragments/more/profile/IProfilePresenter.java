package com.football.fantasy.fragments.more.profile;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IProfilePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getProfile();
}
