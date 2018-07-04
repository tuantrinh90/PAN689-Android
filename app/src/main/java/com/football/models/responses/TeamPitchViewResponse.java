package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TeamPitchViewResponse implements Serializable {

    @JsonProperty("players")
    private List<PlayerResponse> players;
    @JsonProperty("more_players")
    private List<PlayerResponse> morePlayers;
    @JsonProperty("team")
    private TeamResponse team;

    public List<PlayerResponse> getPlayers() {
        return players;
    }

    public List<PlayerResponse> getMorePlayers() {
        return morePlayers;
    }

    public TeamResponse getTeam() {
        return team;
    }
}
