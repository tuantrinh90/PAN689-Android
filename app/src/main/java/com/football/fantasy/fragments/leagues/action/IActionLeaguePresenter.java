package com.football.fantasy.fragments.leagues.action;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.requests.LeagueRequest;

public interface IActionLeaguePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void createLeague(LeagueRequest leagueRequest);

}
