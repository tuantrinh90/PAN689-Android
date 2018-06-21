package com.football.fantasy.fragments.leagues.my_leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.RxUtilities;

public class MyLeagueDataPresenter extends BaseDataPresenter<IMyLeagueView> implements IMyLeaguePresenter<IMyLeagueView> {
    /**
     * @param appComponent
     */
    public MyLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMyLeagues(int page, int perPage) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().getMyLeagues(page, perPage), new ApiCallback<PagingResponse<LeagueResponse>>() {
                @Override
                public void onStart() {
                    v.showLoadingPagingListView(true);
                }

                @Override
                public void onComplete() {
                    v.showLoadingPagingListView(false);
                }

                @Override
                public void onSuccess(PagingResponse<LeagueResponse> leagueResponsePagingResponse) {
                    v.displayLeagues(leagueResponsePagingResponse.getData());
                }

                @Override
                public void onError(String error) {
                    v.displayLeagues(null);
                    v.showMessage(error);
                }
            }));
        });
    }
}
