package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TeamPitchViewResponse implements Serializable {

    @JsonProperty("main_players")
    private List<PlayerResponse> mainPlayers;
    @JsonProperty("minor_players")
    private List<PlayerResponse> minorPlayers;

    public List<PlayerResponse> getMainPlayers() {
        return mainPlayers;
    }

    public List<PlayerResponse> getMinorPlayers() {
        return minorPlayers;
    }
}
