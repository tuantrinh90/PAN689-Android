package com.football.fantasy.fragments.leagues.your_team.player_list;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;

import java.util.List;

public interface IPlayerListView extends IBaseMvpView {

    void displayPlayers(List<PlayerResponse> data, boolean newPlayers);

    void displayStatistic(StatisticResponse statistic);
}
