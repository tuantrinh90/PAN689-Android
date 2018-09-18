package com.football.fantasy.fragments.leagues.league_details.trade_review.sub;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeResponse;

import java.util.List;

public interface ISubTradeReviewView extends IBaseMvpView {
    void displayReviews(List<TradeResponse> list);

    void stopLoading();

}
