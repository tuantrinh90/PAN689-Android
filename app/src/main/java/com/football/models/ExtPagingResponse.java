package com.football.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.StatisticResponse;
import com.football.models.responses.TeamResponse;

public class ExtPagingResponse<T> extends PagingResponse<T> {
    @JsonProperty("statistic")
    private StatisticResponse statistic;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("league")
    private LeagueResponse league;

    public StatisticResponse getStatistic() {
        return statistic;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public LeagueResponse getLeague() {
        return league;
    }
}
