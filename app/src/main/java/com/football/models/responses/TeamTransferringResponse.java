package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TeamTransferringResponse implements Serializable {

    @JsonProperty("players")
    private List<PlayerResponse> players;
    @JsonProperty("injured_players")
    private List<PlayerResponse> injuredPlayers;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("current_transfer_player")
    private Integer currentTransferPlayer;
    @JsonProperty("max_transfer_player")
    private Integer maxTransferPlayer;
    @JsonProperty("transfer_player_left_display")
    private String transferPlayerLeftDisplay;
    @JsonProperty("transfer_deadline")
    private String transferDeadline;
    @JsonProperty("transfer_time_left")
    private Integer transferTimeLeft;

    public List<PlayerResponse> getPlayers() {
        return players;
    }

    public List<PlayerResponse> getInjuredPlayers() {
        return injuredPlayers;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public Integer getCurrentTransferPlayer() {
        return currentTransferPlayer;
    }

    public Integer getMaxTransferPlayer() {
        return maxTransferPlayer;
    }

    public String getTransferPlayerLeftDisplay() {
        return transferPlayerLeftDisplay;
    }

    public String getTransferDeadline() {
        return transferDeadline;
    }

    public Integer getTransferTimeLeft() {
        return transferTimeLeft;
    }

    @Override
    public String toString() {
        return "TeamTransferringResponse{" +
                "players=" + players +
                ", injuredPlayers=" + injuredPlayers +
                ", team=" + team +
                ", currentTransferPlayer=" + currentTransferPlayer +
                ", maxTransferPlayer=" + maxTransferPlayer +
                ", transferPlayerLeftDisplay='" + transferPlayerLeftDisplay + '\'' +
                ", transferDeadline='" + transferDeadline + '\'' +
                ", transferTimeLeft=" + transferTimeLeft +
                '}';
    }
}
