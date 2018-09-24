package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PickTurnResponse implements Serializable {

    @JsonProperty("id")
    private int id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("league_id")
    private int leagueId;
    @JsonProperty("team_id")
    private int teamId;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("round")
    private int round;
    @JsonProperty("order")
    private int order;
    @JsonProperty("start_at")
    private String startAt;
    @JsonProperty("end_at")
    private String endAt;
    @JsonProperty("pick_at")
    private String pickAt;
    @JsonProperty("is_auto")
    private Boolean isAuto;
    @JsonProperty("current_time")
    private String currentTime;
    @JsonProperty("draft_time_left")
    private int draftTimeLeft;
    @JsonProperty("your_turn_in")
    private int yourTurnIn;

    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getLeagueId() {
        return leagueId;
    }

    public int getTeamId() {
        return teamId;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public int getRound() {
        return round;
    }

    public int getOrder() {
        return order;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getPickAt() {
        return pickAt;
    }

    public Boolean getAuto() {
        return isAuto;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public int getDraftTimeLeft() {
        return draftTimeLeft;
    }

    public int getYourTurnIn() {
        return yourTurnIn;
    }
}
