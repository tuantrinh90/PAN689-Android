package com.football.fantasy.fragments.leagues.team_squad.trade.request;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TradeResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class RequestDataPresenter extends BaseDataPresenter<IRequestView> implements IRequestPresenter<IRequestView> {
    /**
     * @param appComponent
     */
    protected RequestDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTradeRequests(int type, int leagueId, int teamId, int page) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("per_page", "10");
            queries.put("page", String.valueOf(page));
            queries.put("league_id", String.valueOf(leagueId));
            queries.put("team_id", String.valueOf(teamId));
            queries.put("type", type == RequestFragment.REQUEST_FROM ? "out" : "in");

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTradeRequests(queries),
                    new ApiCallback<PagingResponse<TradeResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<TradeResponse> response) {
                            v.displayTrades(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
