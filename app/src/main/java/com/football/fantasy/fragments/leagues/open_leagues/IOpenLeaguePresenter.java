package com.football.fantasy.fragments.leagues.open_leagues;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IOpenLeaguePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getOpenLeagues(String orderBy, int page, String query, String numberOfUser);

    void join(Integer leagueId);

}
