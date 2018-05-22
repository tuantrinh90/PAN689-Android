package com.football.fantasy.fragments.notification;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class NotificationDataPresenter extends BaseDataPresenter<INotificationView> implements INotificationPresenter<INotificationView> {
    /**
     * @param appComponent
     */
    protected NotificationDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
