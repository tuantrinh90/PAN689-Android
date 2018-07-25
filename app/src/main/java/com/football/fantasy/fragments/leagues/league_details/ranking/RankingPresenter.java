package com.football.fantasy.fragments.leagues.league_details.ranking;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.RankingResponse;
import com.football.models.responses.ResultResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class RankingPresenter extends BaseDataPresenter<IRankingView> implements IRankingPresenter<IRankingView> {
    /**
     * @param appComponent
     */
    public RankingPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getRanking(Integer leagueId) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getMatchResults(leagueId, queries),
                    new ApiCallback<PagingResponse<RankingResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<RankingResponse> response) {
                            v.displayRanking(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }
}
