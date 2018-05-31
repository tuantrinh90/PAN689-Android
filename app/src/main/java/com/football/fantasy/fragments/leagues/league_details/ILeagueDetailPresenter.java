package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ILeagueDetailPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getLeagueDetail(int leagueId);

}
