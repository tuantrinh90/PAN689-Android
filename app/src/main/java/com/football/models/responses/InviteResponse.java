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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public Integer getStatus() {
        return status == null ? 0 : status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDecisionAt() {
        return decisionAt + "";
    }

    public void setDecisionAt(String decisionAt) {
        this.decisionAt = decisionAt;
    }

    @Override
    public String toString() {
        return "InviteResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", leagueId=" + leagueId +
                ", status=" + status +
                ", decisionAt='" + decisionAt + '\'' +
                '}';
    }
}
