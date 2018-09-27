package com.football.fantasy.fragments.notification;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.NotificationResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class NotificationPresenter extends BaseDataPresenter<INotificationView> implements INotificationPresenter<INotificationView> {
    /**
     * @param appComponent
     */
    protected NotificationPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getNotification(String deviceId, int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("page", String.valueOf(page));
            queries.put("device_id", deviceId);
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getNotifications(queries),
                    new ApiCallback<PagingResponse<NotificationResponse>>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<NotificationResponse> response) {
                            v.displayNotifications(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
