package com.football.fantasy.fragments.leagues.your_team.team_list;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface ITeamListView extends IBaseMvpView {

    void displayTeams(List<TeamResponse> teams);

}
