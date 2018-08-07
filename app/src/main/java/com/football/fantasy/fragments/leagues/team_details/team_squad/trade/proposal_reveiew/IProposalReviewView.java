package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TradeResponse;

public interface IProposalReviewView extends IBaseMvpView {
    void submitSuccess(TradeResponse response);
}
