package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PropsPlayerResponse implements Serializable {


    @JsonProperty("team")
    private TeamResponse team;

    public TeamResponse getTeam() {
        return team;
    }
}
