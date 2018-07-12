package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyMatchResponse {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("with_team")
    private TeamResponse withTeam;
    @JsonProperty("league")
    private LeagueResponse league;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("start_at")
    private String startAt;
    @JsonProperty("end_at")
    private String endAt;

    public Integer getId() {
        return id;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public TeamResponse getWithTeam() {
        return withTeam;
    }

    public LeagueResponse getLeague() {
        return league;
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
}
