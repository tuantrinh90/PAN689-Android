package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ResultResponse implements Serializable {

    @JsonProperty("ranking")
    private Integer ranking;
    @JsonProperty("played")
    private Integer played;
    @JsonProperty("win")
    private Integer win;
    @JsonProperty("draw")
    private Integer draw;
    @JsonProperty("lose")
    private Integer lose;
    @JsonProperty("points")
    private Integer points;

    public Integer getRanking() {
        return ranking;
    }

    public Integer getPlayed() {
        return played;
    }

    public Integer getWin() {
        return win;
    }

    public Integer getDraw() {
        return draw;
    }

    public Integer getLose() {
        return lose;
    }

    public Integer getPoints() {
        return points;
    }
}
