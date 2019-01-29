package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MaxRoundResponse implements Serializable {
    @JsonProperty("max_round")
    private Integer maxRound;

    public Integer getMaxRound() {
        return maxRound;
    }
}
