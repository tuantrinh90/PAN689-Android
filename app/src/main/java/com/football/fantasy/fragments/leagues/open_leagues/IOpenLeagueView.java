package com.football.fantasy.fragments.leagues.open_leagues;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

import java.util.List;

public interface IOpenLeagueView extends IBaseMvpView {
    void displayLeagues(List<LeagueResponse> its);

    void refreshData(Integer leagueId);

    void stopLoading();

    void onJoinSuccessful(LeagueResponse response);

}
