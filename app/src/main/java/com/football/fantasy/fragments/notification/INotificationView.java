package com.football.fantasy.fragments.notification;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.NotificationResponse;

import java.util.List;

public interface INotificationView extends IBaseMvpView {
    void displayNotifications(List<NotificationResponse> data);

    void stopLoading();

}
