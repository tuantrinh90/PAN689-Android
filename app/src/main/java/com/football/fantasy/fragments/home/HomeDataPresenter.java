package com.football.fantasy.fragments.home;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
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
    public void getMyLeagues() {

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
