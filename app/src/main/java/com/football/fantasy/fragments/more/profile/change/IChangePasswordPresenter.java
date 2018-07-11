package com.football.fantasy.fragments.more.profile.change;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IChangePasswordPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void changePassword(String oldPassword, String newPassword);
}
