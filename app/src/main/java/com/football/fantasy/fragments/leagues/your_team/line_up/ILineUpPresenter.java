package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

public interface ILineUpPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getLineup(int teamId);

    void addPlayer(PlayerResponse player, int teamId, int position, int order);

    void removePlayer(PlayerResponse player, int position, int teamId);

}
