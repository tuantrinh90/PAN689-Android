package com.football.fantasy.fragments.leagues.league_details.results;

import android.text.TextUtils;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.MatchResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

import static com.football.utilities.Constant.ROUND_DEFAULT;

public class ResultsPresenter extends BaseDataPresenter<IResultsView> implements IResultsPresenter<IResultsView> {
    /**
     * @param appComponent
     */
    public ResultsPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMatchResults(Integer leagueId, String round) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            if (!TextUtils.isEmpty(round)) {
                queries.put("round", round);
            }

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
                            v.displayTotalRound(response.getTotalRound());
                            v.displayMatches(response.getData());
                            v.displayRound(response.getRound());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error, R.string.ok, null);
                        }
                    }));
        });
    }
}
