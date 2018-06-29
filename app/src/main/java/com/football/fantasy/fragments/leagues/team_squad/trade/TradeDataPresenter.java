package com.football.fantasy.fragments.leagues.team_squad.trade;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class TradeDataPresenter extends BaseDataPresenter<ITradeView> implements ITradePresenter<ITradeView> {
    /**
     * @param appComponent
     */
    protected TradeDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
