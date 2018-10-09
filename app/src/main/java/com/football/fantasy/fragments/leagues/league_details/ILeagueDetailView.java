package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;

public interface ILeagueDetailView extends IBaseMvpView {
    void displayMenu(LeagueResponse response);

    void displayLeaguePager(LeagueResponse response);

    void displayLeague(LeagueResponse league);

    void handleActionNotification(boolean goLineup);

    void goCreateTeam();

    void goLineup();

    void stopOrLeaveLeagueSuccess();

    void handleLessThan18Players(ArrayList<Integer> playerIds, long value);

    void handleMoreThan18Players(int numberPlayer);
}
