package com.football.fantasy.fragments.leagues.player_pool;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.SeasonResponse;

import java.util.List;

public interface IPlayerPoolView extends IBaseMvpView {
    void displayPlayers(List<PlayerResponse> players);

    void displaySeasons(List<SeasonResponse> seasons);

    void handleTransferSuccess(int deletedPlayerId);
}
