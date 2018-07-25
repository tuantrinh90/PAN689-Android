package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RankingResponse implements Serializable {

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
    private Object formation;
    @JsonProperty("pick_order")
    private Integer pickOrder;
    @JsonProperty("result")
    private ResultResponse result;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public UserResponse getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLogo() {
        return logo;
    }

    public Integer getCurrentBudget() {
        return currentBudget;
    }

    public Integer getRank() {
        return rank;
    }

    public Integer getTotalPoint() {
        return totalPoint;
    }

    public Object getFormation() {
        return formation;
    }

    public Integer getPickOrder() {
        return pickOrder;
    }

    public ResultResponse getResult() {
        return result;
    }
}
