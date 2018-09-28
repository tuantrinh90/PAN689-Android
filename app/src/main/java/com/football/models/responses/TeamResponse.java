package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.AppUtilities;

import java.io.Serializable;

public class TeamResponse implements Serializable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("user")
    private UserResponse user;
    @JsonProperty("statistic")
    private PlayerStatisticMetaResponse statistic;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("current_budget")
    private Long currentBudget;
    @JsonProperty("rank")
    private int rank;
    @JsonProperty("total_point")
    private int totalPoint;
    @JsonProperty("formation")
    private String formation;
    @JsonProperty("pick_order")
    private int pickOrder;
    @JsonProperty("is_owner")
    private Boolean isOwner;
    @JsonProperty("is_completed")
    private Boolean isCompleted;
    @JsonProperty("point")
    private int point;
    @JsonProperty("round")
    private int round;
    @JsonProperty("current_trade_request")
    private int currentTradeRequest;
    @JsonProperty("max_trade_request")
    private int maxTradeRequest;
    @JsonProperty("trade_request_left_display")
    private String tradeRequestLeftDisplay;

    @JsonProperty("current_round")
    private TeamRoundResponse currentRound;
    @JsonProperty("last_round")
    private TeamRoundResponse lastRound;

    @JsonIgnore
    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public PlayerStatisticMetaResponse getStatistic() {
        return statistic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public Long getCurrentBudget() {
        return currentBudget == null ? 0 : currentBudget;
    }

    public String getCurrentBudgetValue() {
        return AppUtilities.getMoney(getCurrentBudget());
    }

    public void setCurrentBudget(Long currentBudget) {
        this.currentBudget = currentBudget;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getFormation() {
        return formation + "";
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public int getPickOrder() {
        return pickOrder;
    }

    public void setPickOrder(int pickOrder) {
        this.pickOrder = pickOrder;
    }

    public Boolean getOwner() {
        return isOwner == null ? false : isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public Boolean getCompleted() {
        return isCompleted == null ? false : isCompleted;
    }

    public int getPoint() {
        return point;
    }

    public int getRound() {
        return round;
    }

    public int getCurrentTradeRequest() {
        return currentTradeRequest;
    }

    public int getMaxTradeRequest() {
        return maxTradeRequest;
    }

    public String getTradeRequestLeftDisplay() {
        return tradeRequestLeftDisplay;
    }

    public TeamRoundResponse getCurrentRound() {
        return currentRound;
    }

    public TeamRoundResponse getLastRound() {
        return lastRound;
    }

    @Override
    public String toString() {
        return "TeamResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", statistic=" + statistic +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", currentBudget=" + currentBudget +
                ", rank=" + rank +
                ", totalPoint=" + totalPoint +
                ", formation='" + formation + '\'' +
                ", pickOrder=" + pickOrder +
                ", isOwner=" + isOwner +
                ", isCompleted=" + isCompleted +
                ", point=" + point +
                ", round=" + round +
                ", currentTradeRequest=" + currentTradeRequest +
                ", maxTradeRequest=" + maxTradeRequest +
                ", tradeRequestLeftDisplay='" + tradeRequestLeftDisplay + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
