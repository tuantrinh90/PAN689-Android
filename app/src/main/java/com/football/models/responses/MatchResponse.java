package com.football.models.responses;

import com.bon.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.Constant;

public class MatchResponse {

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

    public String getStartAtFormatted() {
        return DateTimeUtils.convertCalendarToString(DateTimeUtils.convertStringToCalendar(
                startAt,
                Constant.FORMAT_DATE_TIME_SERVER),
                Constant.FORMAT_DATE_TIME);
    }
}
