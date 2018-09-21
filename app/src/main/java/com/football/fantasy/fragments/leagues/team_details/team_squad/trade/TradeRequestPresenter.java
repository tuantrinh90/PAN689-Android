package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TradeRequestPresenter extends BaseDataPresenter<ITradeRequestView> implements ITradeRequestPresenter<ITradeRequestView> {
    /**
     * @param appComponent
     */
    protected TradeRequestPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}