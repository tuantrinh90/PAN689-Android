package com.football.fantasy.fragments.leagues.action.team;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.requests.TeamRequest;

public interface ICreateTeamPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void createTeam(TeamRequest request);

}
