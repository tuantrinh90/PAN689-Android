package com.football.fantasy.fragments.leagues.league_details.successor;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ISuccessorPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTeams(int leagueId);

    void leaveLeague(int leagueId, int teamId);
}
