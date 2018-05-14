package com.football.fantasy.fragments.notification;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

public class NotificationFragment extends BaseMvpFragment<INotificationView, INotificationPresenter<INotificationView>> implements INotificationView {
    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.notification_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public INotificationPresenter<INotificationView> createPresenter() {
        return new NotificationPresenter(getAppComponent());
    }
}
