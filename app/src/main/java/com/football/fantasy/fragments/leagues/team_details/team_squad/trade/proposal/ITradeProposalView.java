package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeResponse;

public interface ITradeProposalView extends IBaseMvpView {
    void submitSuccess(TradeResponse response);
}
