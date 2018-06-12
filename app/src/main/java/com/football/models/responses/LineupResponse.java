package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class LineupResponse implements Serializable {

    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("league")
    private LeagueResponse league;
    @JsonProperty("players")
    private List<PlayerResponse> players;
    @JsonProperty("statistic")
    private StatisticResponse statistic;

    public TeamResponse getTeam() {
        return team;
    }

    public LeagueResponse getLeague() {
        return league;
    }

    public List<PlayerResponse> getPlayers() {
        return players;
    }

    public StatisticResponse getStatistic() {
        return statistic;
    }
}
