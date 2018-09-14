package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RoundResponse implements Serializable {

    @JsonProperty("round")
    private int round;
    @JsonProperty("point")
    private int point;
    @JsonProperty("change")
    private int change;

    public int getRound() {
        return round;
    }

    public int getPoint() {
        return point;
    }

    public int getChange() {
        return change;
    }

}
