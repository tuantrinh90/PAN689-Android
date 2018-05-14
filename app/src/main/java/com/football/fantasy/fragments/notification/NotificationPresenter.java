package com.football.fantasy.fragments.notification;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class NotificationPresenter extends BaseDataPresenter<INotificationView> implements INotificationPresenter<INotificationView> {
    /**
     * @param appComponent
     */
    protected NotificationPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
