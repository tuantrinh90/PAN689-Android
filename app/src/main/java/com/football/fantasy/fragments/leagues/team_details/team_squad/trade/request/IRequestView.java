package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeResponse;

import java.util.List;

public interface IRequestView extends IBaseMvpView {

    void displayTradeRequestLeftDisplay(String tradeRequestLeftDisplay);

    void displayTrades(List<TradeResponse> trades);

}
