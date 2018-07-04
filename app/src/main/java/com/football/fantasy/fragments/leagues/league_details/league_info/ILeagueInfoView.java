package com.football.fantasy.fragments.leagues.league_details.league_info;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

public interface ILeagueInfoView extends IBaseMvpView {
    void displayLeague(LeagueResponse league);

    void onAcceptSuccess();
}
