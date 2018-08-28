package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PickHistoryResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("team_id")
    private Integer teamId;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("player_id")
    private Integer playerId;
    @JsonProperty("player")
    private PlayerResponse player;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("pick_at")
    private String pickAt;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public PlayerResponse getPlayer() {
        return player;
    }

    public Integer getRound() {
        return round;
    }

    public String getPickAt() {
        return pickAt;
    }
}
