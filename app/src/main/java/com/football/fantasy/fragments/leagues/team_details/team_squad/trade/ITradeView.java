package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

public interface ITradeView extends IBaseMvpView {
    void displayTeam(TeamResponse response);
}
