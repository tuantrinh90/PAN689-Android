package com.football.fantasy.fragments.more.profile.edit;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IEditProfilePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getProfile();
}
