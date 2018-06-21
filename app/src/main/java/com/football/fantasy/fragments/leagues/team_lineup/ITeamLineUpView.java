package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface ITeamLineUpView extends IBaseMvpView {
    void displayMainPlayers(List<PlayerResponse> players);

    void displayMinorPlayers(List<PlayerResponse> players);
}
