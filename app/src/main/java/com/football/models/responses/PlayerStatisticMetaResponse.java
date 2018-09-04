package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PlayerStatisticMetaResponse implements Serializable {
    @JsonProperty("goals")
    private float goals;
    @JsonProperty("assists")
    private float assists;
    @JsonProperty("clean_sheet")
    private float cleanSheet;
    @JsonProperty("duels_they_win")
    private float duelsTheyWin;
    @JsonProperty("passes")
    private float passes;
    @JsonProperty("shots")
    private float shots;
    @JsonProperty("saves")
    private float saves;
    @JsonProperty("yellow_cards")
    private float yellowCards;
    @JsonProperty("dribbles")
    private float dribbles;
    @JsonProperty("turnovers")
    private float turnovers;
    @JsonProperty("balls_recovered")
    private float ballsRecovered;
    @JsonProperty("fouls_committed")
    private float foulsCommitted;
    @JsonProperty("point")
    private float point;

    public Integer getGoals() {
        return Math.round(goals);
    }

    public Integer getAssists() {
        return Math.round(assists);
    }

    public Integer getCleanSheet() {
        return Math.round(cleanSheet);
    }

    public Integer getDuelsTheyWin() {
        return Math.round(duelsTheyWin);
    }

    public Integer getPasses() {
        return Math.round(passes);
    }

    public Integer getShots() {
        return Math.round(shots);
    }

    public Integer getSaves() {
        return Math.round(saves);
    }

    public Integer getYellowCards() {
        return Math.round(yellowCards);
    }

    public Integer getDribbles() {
        return Math.round(dribbles);
    }

    public Integer getTurnovers() {
        return Math.round(turnovers);
    }

    public Integer getBallsRecovered() {
        return Math.round(ballsRecovered);
    }

    public Integer getFoulsCommitted() {
        return Math.round(foulsCommitted);
    }

    public Integer getPoint() {
        return Math.round(point);
    }
}
