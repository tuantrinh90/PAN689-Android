package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface ILineUpPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getLineup(int leagueId);

    void addPlayer(int teamId, Integer playerId);
}
