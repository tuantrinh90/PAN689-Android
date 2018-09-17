package com.football.fantasy.fragments.leagues.league_details.teams;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface ITeamView extends IBaseMvpView {
    void displayTeams(List<TeamResponse> teams);

    void hideLoading();

    void removeSuccess(int teamId);

}
