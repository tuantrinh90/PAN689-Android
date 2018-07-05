package com.football.fantasy.fragments.leagues.team_preview;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface ILineupPreviewView extends IBaseMvpView {

    void displayTeamState(TeamResponse team);

    void displayLineupPlayers(List<PlayerResponse> players);

    void displayStatistic(StatisticResponse statistic);

}
