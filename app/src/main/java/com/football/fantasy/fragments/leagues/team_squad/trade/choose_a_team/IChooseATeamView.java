package com.football.fantasy.fragments.leagues.team_squad.trade.choose_a_team;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface IChooseATeamView extends IBaseMvpView {
    void displayTeams(List<TeamResponse> teams);
}
