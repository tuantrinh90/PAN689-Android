package com.football.fantasy.fragments.leagues.my_leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class MyLeagueDataPresenter extends BaseDataPresenter<IMyLeagueView> implements IMyLeaguePresenter<IMyLeagueView> {
    /**
     * @param appComponent
     */
    public MyLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMyLeagues(int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("page", String.valueOf(page));

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getMyLeagues(queries),
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
                            v.displayLeagues(leagueResponsePagingResponse.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
