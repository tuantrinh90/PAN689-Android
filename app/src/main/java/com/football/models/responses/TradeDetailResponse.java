package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TradeDetailResponse implements Serializable{

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("request_id")
    private Integer requestId;
    @JsonProperty("from_player_id")
    private Integer fromPlayerId;
    @JsonProperty("from_player")
    private PlayerResponse fromPlayer;
    @JsonProperty("to_player_id")
    private Integer toPlayerId;
    @JsonProperty("to_player")
    private PlayerResponse toPlayer;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public Integer getFromPlayerId() {
        return fromPlayerId;
    }

    public PlayerResponse getFromPlayer() {
        return fromPlayer;
    }

    public Integer getToPlayerId() {
        return toPlayerId;
    }

    public PlayerResponse getToPlayer() {
        return toPlayer;
    }
}
