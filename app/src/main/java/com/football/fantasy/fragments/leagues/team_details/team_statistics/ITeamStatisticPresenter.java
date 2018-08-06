package com.football.fantasy.fragments.leagues.team_details.team_statistics;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITeamStatisticPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getTeamStatistic(int teamId);

}
