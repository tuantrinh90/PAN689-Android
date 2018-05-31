package com.football.fantasy.fragments.home;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.NewsResponse;
import com.football.utilities.RxUtilities;

public class HomeDataPresenter extends BaseDataPresenter<IHomeView> implements IHomePresenter<IHomeView> {
    /**
     * @param appComponent
     */
    protected HomeDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMyLeagues(int page, int perPage) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().getMyLeagues(page, perPage), new ApiCallback<PagingResponse<LeagueResponse>>() {
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
    public void getNews(int page, int perPage) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().getHomeNews(page, perPage), new ApiCallback<PagingResponse<NewsResponse>>() {
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
