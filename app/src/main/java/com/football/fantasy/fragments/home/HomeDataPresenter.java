package com.football.fantasy.fragments.home;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.NewsResponse;
import com.football.models.responses.NotificationUnreadResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HomeDataPresenter extends BaseDataPresenter<IHomeView> implements IHomePresenter<IHomeView> {
    /**
     * @param appComponent
     */
    protected HomeDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void updateDeviceOfUser(String deviceId, String token) {
        getOptView().doIfPresent(v -> {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("device_id", deviceId)
                    .addFormDataPart("token", token)
                    .addFormDataPart("type", "1")
                    .build();

            // save device token
            AppPreferences.getInstance(v.getAppActivity().getAppContext()).putString(Constant.KEY_DEVICE_TOKEN, token);

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updateDeviceOfUser(body), null));
        });
    }

    @Override
    public void getNotificationsCount() {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getNotificationsUnread(),
                    new ApiCallback<NotificationUnreadResponse>() {
                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(NotificationUnreadResponse response) {
                            v.updateNotificationState(response.getTotal());
                        }

                        @Override
                        public void onError(String error) {
                        }
                    }));
        });
    }

    @Override
    public void getMyLeagues(int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("page", String.valueOf(page));
            queries.put("max_number_user", String.valueOf(5));

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getMyLeagues(queries),
                    new ApiCallback<PagingResponse<LeagueResponse>>() {
                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<LeagueResponse> leagueResponsePagingResponse) {
                            v.notifyDataSetChangedLeagues(leagueResponsePagingResponse.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void getNews(int page) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getHomeNews(page, ExtPagingListView.NUMBER_PER_PAGE),
                    new ApiCallback<PagingResponse<NewsResponse>>() {
                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<NewsResponse> newsResponsePagingResponse) {
                            v.notifyDataSetChangedNews(newsResponsePagingResponse.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
