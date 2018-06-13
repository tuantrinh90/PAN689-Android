package com.football.fantasy.fragments.leagues.player_pool.display;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IPlayerPoolDisplayPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getRealClubs();

}
