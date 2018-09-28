package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RealMatchResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("real_league_id")
    private Integer realLeagueId;
    @JsonProperty("real_league")
    private RealLeagueResponse realLeague;
    @JsonProperty("team_1")
    private String team1;
    @JsonProperty("team_2")
    private String team2;
    @JsonProperty("team_1_result")
    private Integer team1Result;
    @JsonProperty("team_2_result")
    private Integer team2Result;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("start_at")
    private String startAt;
    @JsonProperty("end_at")
    private String endAt;
    @JsonProperty("delay_start_at")
    private String delayStartAt;
    @JsonProperty("delay_end_at")
    private String delayEndAt;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getRealLeagueId() {
        return realLeagueId;
    }

    public RealLeagueResponse getRealLeague() {
        return realLeague;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public Integer getTeam1Result() {
        return team1Result;
    }

    public Integer getTeam2Result() {
        return team2Result;
    }

    public Integer getRound() {
        return round;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getDelayStartAt() {
        return delayStartAt;
    }

    public String getDelayEndAt() {
        return delayEndAt;
    }

    @Override
    public String toString() {
        return "RealMatchResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", realLeagueId=" + realLeagueId +
                ", realLeague=" + realLeague +
                ", team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", team1Result=" + team1Result +
                ", team2Result=" + team2Result +
                ", round=" + round +
                ", startAt='" + startAt + '\'' +
                ", endAt='" + endAt + '\'' +
                '}';
    }
}
