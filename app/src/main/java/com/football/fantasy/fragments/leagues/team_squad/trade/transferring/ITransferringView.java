package com.football.fantasy.fragments.leagues.team_squad.trade.transferring;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface ITransferringView extends IBaseMvpView {
    void displayPlayers(List<PlayerResponse> players);

    void hideRecyclerViewLoading();
}
