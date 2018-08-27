package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_teams;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface IDraftTeamsView extends IBaseMvpView {

    void displayTeams(List<TeamResponse> teams);
}
