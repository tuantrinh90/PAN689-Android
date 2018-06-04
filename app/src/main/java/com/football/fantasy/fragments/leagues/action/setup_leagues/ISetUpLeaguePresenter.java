package com.football.fantasy.fragments.leagues.action.setup_leagues;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.requests.LeagueRequest;

public interface ISetUpLeaguePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getBudgets(int leagueId);

    void createLeague(LeagueRequest leagueRequest);

    void updateLeague(int leagueId, LeagueRequest leagueRequest);

}
