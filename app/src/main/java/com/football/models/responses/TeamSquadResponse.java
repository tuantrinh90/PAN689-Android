package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TeamSquadResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("total_point")
    private Integer totalPoint;
    @JsonProperty("current_budget")
    private Long currentBudget;
    @JsonProperty("league")
    private LeagueResponse league;
    @JsonProperty("players")
    private List<PlayerResponse> players;
    @JsonProperty("my_team")
    private TeamResponse myTeam;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public Long getCurrentBudget() {
        return currentBudget;
    }

    public LeagueResponse getLeague() {
        return league;
    }

    public List<PlayerResponse> getPlayers() {
        return players;
    }

    public TeamResponse getMyTeam() {
        return myTeam;
    }
}
