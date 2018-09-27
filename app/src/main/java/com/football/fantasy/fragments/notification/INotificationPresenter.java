package com.football.fantasy.fragments.notification;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface INotificationPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getNotification(String devicedId, int page);

}
