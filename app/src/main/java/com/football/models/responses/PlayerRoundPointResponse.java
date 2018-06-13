package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerRoundPointResponse {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("point")
    private Integer point;
    @JsonProperty("change")
    private Integer change;

    public Integer getId() {
        return id;
    }

    public Integer getRound() {
        return round;
    }

    public Integer getPoint() {
        return point;
    }

    public Integer getChange() {
        return change;
    }
}
