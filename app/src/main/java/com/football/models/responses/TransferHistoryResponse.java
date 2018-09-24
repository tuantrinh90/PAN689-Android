package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.AppUtilities;

import java.io.Serializable;

public class TransferHistoryResponse implements Serializable {

    public static final int STATUS_OUT = -1;
    public static final int PENDING = 0;
    public static final int STATUS_IN = 1;

    @JsonProperty("id")
    private int id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("from_player_id")
    private int fromPlayerId;
    @JsonProperty("from_player")
    private PlayerResponse fromPlayer;
    @JsonProperty("to_player_id")
    private int toPlayerId;
    @JsonProperty("to_player")
    private PlayerResponse toPlayer;
    @JsonProperty("status")
    private int status;
    @JsonProperty("status_display")
    private String statusDisplay;
    @JsonProperty("transfer_fee")
    private Long transferFee;
    @JsonProperty("transfer_at")
    private String transferAt;
    @JsonProperty("with_team_id")
    private int withTeamId;
    @JsonProperty("with_team")
    private TeamResponse withTeam;

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getFromPlayerId() {
        return fromPlayerId;
    }

    public PlayerResponse getFromPlayer() {
        return fromPlayer;
    }

    public int getToPlayerId() {
        return toPlayerId;
    }

    public PlayerResponse getToPlayer() {
        return toPlayer;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public Long getTransferFee() {
        return transferFee;
    }

    public String getTransferFeeValue() {
        return AppUtilities.getMoney(transferFee);
    }

    public String getTransferAt() {
        return transferAt;
    }

    public int getWithTeamId() {
        return withTeamId;
    }

    public TeamResponse getWithTeam() {
        return withTeam;
    }
}
