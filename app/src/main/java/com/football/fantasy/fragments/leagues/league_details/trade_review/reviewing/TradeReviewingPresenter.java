package com.football.fantasy.fragments.leagues.league_details.trade_review.reviewing;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TradeResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class TradeReviewingPresenter extends BaseDataPresenter<ITradeReviewingView> implements ITradeReviewingPresenter<ITradeReviewingView> {
    /**
     * @param appComponent
     */
    protected TradeReviewingPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getReviews(int leagueId, int page, String type) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("page", String.valueOf(page));
            queries.put("league_id", String.valueOf(leagueId));
            queries.put("type", type);
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTradeReviews(queries),
                    new ApiCallback<PagingResponse<TradeResponse>>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                            v.stopLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<TradeResponse> response) {
                            v.displayReviews(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
