package com.football.fantasy.fragments.leagues.team_details.gameplay_option.transferring;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface ITransferringView extends IBaseMvpView {
    void displayPlayers(List<PlayerResponse> players);

    void displayInjuredPlayers(List<PlayerResponse> injuredPlayers);

    void displayHeader(boolean canTransfer, int current, int max, long transferTimeLeft, long budget);

    void transferSuccess();

    void transferError(String error);
}
