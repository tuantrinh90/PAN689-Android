package com.football.fantasy.fragments.leagues.open_leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.RxUtilities;

public class OpenLeagueDataPresenter extends BaseDataPresenter<IOpenLeagueView> implements IOpenLeaguePresenter<IOpenLeagueView> {
    /**
     * @param appComponent
     */
    public OpenLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getOpenLeagues(String orderBy, int page, int perPage, String query) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().getOpenLeagues(orderBy, page, perPage, query),
                    new ApiCallback<PagingResponse<LeagueResponse>>() {
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
                            v.notifyDataSetChangedLeagues(leagueResponsePagingResponse.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.notifyDataSetChangedLeagues(null);
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
