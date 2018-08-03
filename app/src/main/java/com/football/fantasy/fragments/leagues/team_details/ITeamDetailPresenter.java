package com.football.fantasy.fragments.leagues.team_details;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITeamDetailPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTeam(int teamId);
}
