package com.football.fantasy.fragments.leagues.my_leagues;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IMyLeaguePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getMyLeagues(int page, int perPage);
}
