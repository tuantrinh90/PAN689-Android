package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("current_budget")
    private Integer currentBudget;
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

    public Integer getCurrentBudget() {
        return currentBudget == null ? 0 : currentBudget;
    }

    public void setCurrentBudget(Integer currentBudget) {
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

    @Override
    public String toString() {
        return "TeamResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", currentBudget=" + currentBudget +
                ", rank=" + rank +
                ", totalPoint=" + totalPoint +
                ", formation='" + formation + '\'' +
                ", pickOrder=" + pickOrder +
                ", isOwner=" + isOwner +
                ", isChecked=" + isChecked +
                '}';
    }
}
