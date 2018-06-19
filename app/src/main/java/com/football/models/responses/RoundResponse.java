package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RoundResponse implements Serializable {

    @JsonProperty("round")
    private Integer round;
    @JsonProperty("point")
    private Integer point;
    @JsonProperty("change")
    private Integer change;

    public Integer getRound() {
        return round;
    }

    public Integer getPoint() {
        return point;
    }

    public Integer getChange() {
        return change;
    }

    public boolean isIncrease() {
        return point > 0;
    }
}
