package com.football.fantasy.fragments.leagues.team_details.gameplay_option.request;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeResponse;

import java.util.List;

public interface IRequestView extends IBaseMvpView {

    void displayTrades(List<TradeResponse> trades);
}
