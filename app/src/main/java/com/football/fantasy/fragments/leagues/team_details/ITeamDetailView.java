package com.football.fantasy.fragments.leagues.team_details;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

public interface ITeamDetailView extends IBaseMvpView {
    void displayTeam(TeamResponse team);
}
