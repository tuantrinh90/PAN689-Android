package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.AppUtilities;

import java.io.Serializable;

public class TeamResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("user_id")
    private Integer userId;
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
    private Integer rank;
    @JsonProperty("total_point")
    private Integer totalPoint;
    @JsonProperty("formation")
    private String formation;
    @JsonProperty("pick_order")
    private Integer pickOrder;
    @JsonProperty("is_owner")
    private Boolean isOwner;
    @JsonProperty("is_completed")
    private Boolean isCompleted;
    @JsonProperty("point")
    private Integer point;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("current_trade_request")
    private Integer currentTradeRequest;
    @JsonProperty("max_trade_request")
    private Integer maxTradeRequest;
    @JsonProperty("trade_request_left_display")
    private String tradeRequestLeftDisplay;

    @JsonProperty("current_round")
    private TeamRoundResponse currentRound;

    @JsonIgnore
    private boolean isChecked;

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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
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

    public Integer getRank() {
        return rank == null ? 0 : rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getTotalPoint() {
        return totalPoint == null ? 0 : totalPoint;
    }

    public void setTotalPoint(Integer totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getFormation() {
        return formation + "";
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public Integer getPickOrder() {
        return pickOrder == null ? 0 : pickOrder;
    }

    public void setPickOrder(Integer pickOrder) {
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

    public Integer getPoint() {
        return point;
    }

    public Integer getRound() {
        return round;
    }

    public Integer getCurrentTradeRequest() {
        return currentTradeRequest;
    }

    public Integer getMaxTradeRequest() {
        return maxTradeRequest;
    }

    public String getTradeRequestLeftDisplay() {
        return tradeRequestLeftDisplay;
    }

    public TeamRoundResponse getCurrentRound() {
        return currentRound;
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
