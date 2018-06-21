package com.football.fantasy.fragments.leagues.your_team.players_popup;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface IPlayerPopupView extends IBaseMvpView {
    void displayPlayers(List<PlayerResponse> data);
}
