package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class TurnResponse implements Serializable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("active")
    private boolean active;
    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("league_id")
    private int leagueId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("current_budget")
    private Object currentBudget;
    @JsonProperty("rank")
    private int rank;
    @JsonProperty("total_point")
    private int totalPoint;
    @JsonProperty("formation")
    private String formation;
    @JsonProperty("pick_order")
    private int pickOrder;
    @JsonProperty("total_players")
    private int totalPlayers;
    @JsonProperty("is_completed")
    private boolean isCompleted;
    @JsonProperty("current_transfer_player")
    private int currentTransferPlayer;
    @JsonProperty("current_trade_request")
    private int currentTradeRequest;
    @JsonProperty("player")
    private int player;
    @JsonProperty("current")
    private boolean current;
    @JsonProperty("next")
    private boolean next;
    @JsonProperty("show_pick_order")
    private int showPickOrder;

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public boolean isActive() {
        return active;
    }

    public int getUserId() {
        return userId;
    }

    public int getLeagueId() {
        return leagueId;
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

    public Object getCurrentBudget() {
        return currentBudget;
    }

    public int getRank() {
        return rank;
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public String getFormation() {
        return formation;
    }

    public int getPickOrder() {
        return pickOrder;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public int getCurrentTransferPlayer() {
        return currentTransferPlayer;
    }

    public int getCurrentTradeRequest() {
        return currentTradeRequest;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isCurrent() {
        return current;
    }

    public boolean isNext() {
        return next;
    }

    public int getShowPickOrder() {
        return showPickOrder;
    }
}
