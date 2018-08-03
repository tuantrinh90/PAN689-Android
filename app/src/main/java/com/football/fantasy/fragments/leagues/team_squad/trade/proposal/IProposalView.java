package com.football.fantasy.fragments.leagues.team_squad.trade.proposal;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TradeResponse;

import java.util.List;

public interface IProposalView extends IBaseMvpView {
    void submitSuccess(TradeResponse response);
}
