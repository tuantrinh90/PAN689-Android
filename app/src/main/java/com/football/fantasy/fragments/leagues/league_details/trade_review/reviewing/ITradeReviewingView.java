package com.football.fantasy.fragments.leagues.league_details.trade_review.reviewing;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.NotificationResponse;
import com.football.models.responses.TradeResponse;

import java.util.List;

public interface ITradeReviewingView extends IBaseMvpView {
    void displayReviews(List<TradeResponse> list);

    void stopLoading();

}
