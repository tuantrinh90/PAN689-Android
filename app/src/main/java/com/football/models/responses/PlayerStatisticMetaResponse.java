package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PlayerStatisticMetaResponse implements Serializable {
    @JsonProperty("goals")
    private Integer goals;
    @JsonProperty("assists")
    private Integer assists;
    @JsonProperty("clean_sheet")
    private Integer cleanSheet;
    @JsonProperty("duels_they_win")
    private Integer duelsTheyWin;
    @JsonProperty("passes")
    private Integer passes;
    @JsonProperty("shots")
    private Integer shots;
    @JsonProperty("saves")
    private Integer saves;
    @JsonProperty("yellow_cards")
    private Integer yellowCards;
    @JsonProperty("dribbles")
    private Integer dribbles;
    @JsonProperty("turnovers")
    private Integer turnovers;
    @JsonProperty("balls_recovered")
    private Integer ballsRecovered;
    @JsonProperty("fouls_committed")
    private Integer foulsCommitted;

    public Integer getGoals() {
        return goals;
    }

    public Integer getAssists() {
        return assists;
    }

    public Integer getCleanSheet() {
        return cleanSheet;
    }

    public Integer getDuelsTheyWin() {
        return duelsTheyWin;
    }

    public Integer getPasses() {
        return passes;
    }

    public Integer getShots() {
        return shots;
    }

    public Integer getSaves() {
        return saves;
    }

    public Integer getYellowCards() {
        return yellowCards;
    }

    public Integer getDribbles() {
        return dribbles;
    }

    public Integer getTurnovers() {
        return turnovers;
    }

    public Integer getBallsRecovered() {
        return ballsRecovered;
    }

    public Integer getFoulsCommitted() {
        return foulsCommitted;
    }
}
