package com.football.fantasy.fragments.match_up.my;

import com.bon.customview.listview.ExtPagingListView;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.MatchResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class MatchupMyDataLeaguePresenter extends BaseDataPresenter<IMatchupMyLeagueView> implements IMatchupMyLeaguePresenter<IMatchupMyLeagueView> {
    /**
     * @param appComponent
     */
    protected MatchupMyDataLeaguePresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMatchResults(int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("page", String.valueOf(page));
            queries.put("per_page", String.valueOf(ExtPagingListView.NUMBER_PER_PAGE));
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getMyMatchResults(queries),
                    new ApiCallback<PagingResponse<MatchResponse>>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<MatchResponse> response) {
                            v.displayMatches(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
