package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class InvitationResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("sender_id")
    private Integer senderId;
    @JsonProperty("sender")
    private UserResponse sender;
    @JsonProperty("receiver_id")
    private Integer receiverId;
    @JsonProperty("receiver")
    private UserResponse receiver;
    @JsonProperty("league_id")
    private Integer leagueId;

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

    public UserResponse getSender() {
        return sender;
    }

    public void setSender(UserResponse sender) {
        this.sender = sender;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public UserResponse getReceiver() {
        return receiver;
    }

    public void setReceiver(UserResponse receiver) {
        this.receiver = receiver;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    @Override
    public String toString() {
        return "InvitationResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", senderId=" + senderId +
                ", sender=" + sender +
                ", receiverId=" + receiverId +
                ", receiver=" + receiver +
                ", leagueId=" + leagueId +
                '}';
    }
}
