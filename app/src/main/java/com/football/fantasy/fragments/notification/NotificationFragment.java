package com.football.fantasy.fragments.notification;

import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.NotificationAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.models.responses.NotificationResponse;

import java.util.List;

import butterknife.BindView;

public class NotificationFragment extends BaseMvpFragment<INotificationView, INotificationPresenter<INotificationView>> implements INotificationView {

    private String deviceId;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @BindView(R.id.rv_notification)
    ExtRecyclerView<NotificationResponse> rvNotification;

    private int page = 1;

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
    protected void initialized() {
        super.initialized();

        deviceId = Settings.Secure.getString(mActivity.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        initRecyclerView();
        refresh();
    }

    @NonNull
    @Override
    public INotificationPresenter<INotificationView> createPresenter() {
        return new NotificationPresenter(getAppComponent());
    }

    private void initRecyclerView() {
        NotificationAdapter adapter = new NotificationAdapter(
                getContext(),
                notification -> {
                    NotificationResponse.Key key = notification.getKey();
                    ((MainActivity) mActivity).handleAction(
                            key.getAction(),
                            key.getLeagueId(),
                            key.getLeagueStatus(),
                            key.getTeamId(),
                            key.getTeamName(),
                            key.getMyTeamId(),
                            key.getPlayerId());
                });
        rvNotification
                .adapter(adapter)
                .refreshListener(this::refresh)
                .loadMoreListener(() -> {
                    page++;
                    getNotifications();
                })
                .build();
    }

    private void getNotifications() {
        presenter.getNotification(deviceId, page);
    }

    private void refresh() {
        page = 1;
        rvNotification.clear();
        rvNotification.startLoading();
        getNotifications();
    }

    @Override
    public void displayNotifications(List<NotificationResponse> data) {
        rvNotification.addItems(data);
    }

    @Override
    public void stopLoading() {
        rvNotification.stopLoading();
    }
}
