package com.football.fantasy.fragments.leagues.player_details;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PlayerRoundPointResponse;
import com.football.models.responses.PlayerStatisticMetaResponse;

import java.util.List;

public interface IPlayerDetailView extends IBaseMvpView {

    void displayPlayer(PlayerResponse player);

    void displayPoints(Integer totalPoint);

    void displayStatistic(PlayerStatisticMetaResponse meta);

    void displayStatistics(List<PlayerRoundPointResponse> metas);

}
