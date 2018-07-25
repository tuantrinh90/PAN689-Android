package com.football.fantasy.fragments.leagues.league_details.trade_review;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TradeReviewPresenter extends BaseDataPresenter<ITradeReviewView> implements ITradeReviewPresenter<ITradeReviewView> {
    /**
     * @param appComponent
     */
    public TradeReviewPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
