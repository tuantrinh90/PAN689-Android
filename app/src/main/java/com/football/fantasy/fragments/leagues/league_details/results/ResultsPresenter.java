package com.football.fantasy.fragments.leagues.league_details.results;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.MatchResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class ResultsPresenter extends BaseDataPresenter<IResultsView> implements IResultsPresenter<IResultsView> {
    /**
     * @param appComponent
     */
    public ResultsPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMatchResults(Integer leagueId) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getMatchResults(leagueId, queries),
                    new ApiCallback<PagingResponse<MatchResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<MatchResponse> response) {
                            v.displayMatches(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }
}
