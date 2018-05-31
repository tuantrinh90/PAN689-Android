package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

public interface ILeagueDetailView extends IBaseMvpView {

    void displayLeague(LeagueResponse league);

}
