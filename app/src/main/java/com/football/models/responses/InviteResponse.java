package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InviteResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("sender_id")
    private Integer senderId;
    @JsonProperty("receiver_id")
    private Integer receiverId;
    @JsonProperty("league_id")
    private Integer leagueId;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("decision_at")
    private String decisionAt;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDecisionAt() {
        return decisionAt;
    }
}
