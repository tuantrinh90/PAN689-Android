package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;

public interface ITeamLineUpPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getPitchView(Integer teamId, String value);

    void addPlayerToPitchView(Integer teamId, int round, PlayerResponse fromPlayer, PlayerResponse toPlayer, int position, int order);

    void changeTeamFormation(TeamResponse team, String formationValue);
}
