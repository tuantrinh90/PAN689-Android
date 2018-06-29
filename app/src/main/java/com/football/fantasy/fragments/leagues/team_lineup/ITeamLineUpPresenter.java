package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

public interface ITeamLineUpPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getPitchView(Integer teamId, String value);

    void addPlayerToPitchView(Integer teamId, PlayerResponse player, Integer position, Integer order);
}
