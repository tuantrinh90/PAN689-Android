package com.football.fantasy.fragments.leagues.team_squad.trade.request;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IRequestPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTradeRequests(int type, int leagueId, int teamId, int page);
}
