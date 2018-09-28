package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TradeResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class TradeRequestPresenter extends BaseDataPresenter<ITradeRequestView> implements ITradeRequestPresenter<ITradeRequestView> {
    /**
     * @param appComponent
     */
    protected TradeRequestPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTradeRequestsToYou(int leagueId, int teamId) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("per_page", "10");
            queries.put("league_id", String.valueOf(leagueId));
            queries.put("team_id", String.valueOf(teamId));
            queries.put("type", "in");

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTradeRequests(queries),
                    new ApiCallback<PagingResponse<TradeResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<TradeResponse> response) {
                            List<TradeResponse> list = StreamSupport.stream(response.getData()).filter(trade -> trade.getTeamId().equals(teamId)).collect(Collectors.toList());
                            if (list.size() > 0) {
                                v.goProposalReview(list.get(0));
                            }
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}