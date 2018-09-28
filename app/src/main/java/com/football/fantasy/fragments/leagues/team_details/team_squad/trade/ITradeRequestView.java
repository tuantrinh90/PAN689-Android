package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeResponse;

public interface ITradeRequestView extends IBaseMvpView {
    void goProposalReview(TradeResponse tradeResponse);
}
