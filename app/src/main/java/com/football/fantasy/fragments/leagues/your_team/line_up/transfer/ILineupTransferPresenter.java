package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpPresenter;
import com.football.fantasy.fragments.leagues.your_team.line_up.ILineUpView;
import com.football.models.responses.PlayerResponse;

public interface ILineupTransferPresenter<V extends ILineUpView> extends ILineUpPresenter<V> {
    void addPlayer(PlayerResponse player, int teamId, int position, int order);

    void removePlayer(PlayerResponse player, int position, int teamId);

}
