package com.football.fantasy.fragments.leagues.player_pool.filter;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IFilterPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getRealClubs();
}
