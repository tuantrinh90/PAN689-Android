package com.football.fantasy.fragments.home;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IHomePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void updateDeviceOfUser(String deviceId, String token);

    void getNotificationsCount();

    void getMyLeagues(int page);

    void getNews(int page);

}
