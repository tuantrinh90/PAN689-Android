package com.football.fantasy.fragments.leagues.team_details.team_lineup;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface ITeamLineupView extends IBaseMvpView {
    void displayTeam(TeamResponse team);

    void displayMainPlayers(List<PlayerResponse> players);

    void displayMinorPlayers(List<PlayerResponse> players);

    void onAddPlayer(PlayerResponse fromPlayer, PlayerResponse toPlayer, Integer position, Integer order);

}
