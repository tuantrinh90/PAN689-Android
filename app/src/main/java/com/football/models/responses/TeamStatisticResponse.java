package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TeamStatisticResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("total_point")
    private Integer totalPoint;
    @JsonProperty("current_budget")
    private long currentBudget;
    @JsonProperty("league")
    private LeagueResponse league;
    @JsonProperty("rounds")
    private List<RoundResponse> rounds;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public long getCurrentBudget() {
        return currentBudget;
    }

    public LeagueResponse getLeague() {
        return league;
    }

    public List<RoundResponse> getRounds() {
        return rounds;
    }
}
