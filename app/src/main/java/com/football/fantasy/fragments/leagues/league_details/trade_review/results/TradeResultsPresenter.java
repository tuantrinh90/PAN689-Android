package com.football.fantasy.fragments.leagues.league_details.trade_review.results;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.NotificationResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class TradeResultsPresenter extends BaseDataPresenter<ITradeResultsView> implements ITradeResultsPresenter<ITradeResultsView> {
    /**
     * @param appComponent
     */
    protected TradeResultsPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
