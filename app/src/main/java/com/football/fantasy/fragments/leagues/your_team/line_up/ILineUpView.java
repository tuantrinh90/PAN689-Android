package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface ILineUpView extends IBaseMvpView {

    void displayTeamState(TeamResponse team);

    void displayLineupPlayers(List<PlayerResponse> players);

    void displayStatistic(StatisticResponse statistic);

    void onAddPlayer(TeamResponse team, PlayerResponse player, int order);

    void onRemovePlayer(TeamResponse team, PlayerResponse player);

    void handleCallback(boolean success, String error);

    void updateStatistic(int position, int value);

    void onCompleteLineup();

}
