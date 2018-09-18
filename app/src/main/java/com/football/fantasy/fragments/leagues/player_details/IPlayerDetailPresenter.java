package com.football.fantasy.fragments.leagues.player_details;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IPlayerDetailPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getPlayerDetail(int playerId);

    void getPlayerStatistic(int playerId, int teamId, String property, String value);

}
