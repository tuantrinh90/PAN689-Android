package com.football.fantasy.fragments.leagues.league_details.teams;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITeamPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getTeams(int leagueId);

    void removeTeam(int leagueId, Integer teamId);
}
