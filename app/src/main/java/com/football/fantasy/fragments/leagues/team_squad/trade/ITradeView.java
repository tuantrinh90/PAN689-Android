package com.football.fantasy.fragments.leagues.team_squad.trade;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeRequestResponse;

public interface ITradeView extends IBaseMvpView {
    void displayTradeRequest(TradeRequestResponse response);
}
