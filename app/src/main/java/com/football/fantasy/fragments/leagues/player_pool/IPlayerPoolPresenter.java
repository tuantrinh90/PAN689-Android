package com.football.fantasy.fragments.leagues.player_pool;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IPlayerPoolPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getPlayers(String positions, String clubs, String displays);

}
