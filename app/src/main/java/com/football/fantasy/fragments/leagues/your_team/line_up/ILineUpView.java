package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;

import java.util.List;

public interface ILineUpView extends IBaseMvpView {

    void displayLineupPlayers(List<PlayerResponse> players);

    void displayStatistic(StatisticResponse statistic);

    void onAddPlayer(PlayerResponse response);
}
