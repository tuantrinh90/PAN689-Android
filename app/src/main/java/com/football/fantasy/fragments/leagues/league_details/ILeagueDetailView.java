package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

public interface ILeagueDetailView extends IBaseMvpView {
    void displayMenu(LeagueResponse response);

    void displayLeaguePager(LeagueResponse response);

    void displayLeague(LeagueResponse league);

    void goCreateTeam();

    void goLineup();

    void stopOrLeaveLeagueSuccess();

}
