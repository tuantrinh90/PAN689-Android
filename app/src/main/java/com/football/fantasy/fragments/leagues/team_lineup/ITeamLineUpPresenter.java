package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ITeamLineUpPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getPitchView(Integer teamId, String value);
}
