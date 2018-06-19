package com.football.fantasy.fragments.leagues.team_statistics;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamStatisticResponse;

public interface ITeamStatisticView extends IBaseMvpView {
    void displayTeamStatistic(TeamStatisticResponse teamStatistic);
}
