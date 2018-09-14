package com.football.fantasy.fragments.leagues.league_details.trade_review.reviewing;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITradeReviewingPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getReviews(int leagueId, int page);

}
