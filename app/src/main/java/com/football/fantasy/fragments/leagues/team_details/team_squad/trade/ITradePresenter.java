package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITradePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTeamDetail(int teamId);
}
