package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TradeResponse implements Serializable {

    public static final String TYPE_REVIEWING = "reviewing";
    public static final String TYPE_REVIEWED = "reviewed";

    // for reviewing
    public static final int STATUS_REJECT = -1;
    public static final int STATUS_ACCEPT = 1;

    // for reviewed
    public static final int STATUS_FAILED = -1;
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_SUCCESSFUL = 1;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("deadline")
    private String deadline;
    @JsonProperty("review_deadline")
    private String reviewDeadline;
    @JsonProperty("league_id")
    private Integer leagueId;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("team_id")
    private Integer teamId;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("with_team_id")
    private Integer withTeamId;
    @JsonProperty("with_team")
    private TeamResponse withTeam;
    @JsonProperty("total_review")
    private Integer totalReview;
    @JsonProperty("total_approval")
    private Integer totalApproval;
    @JsonProperty("total_rejection")
    private Integer totalRejection;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("status_display")
    private String statusDisplay;
    @JsonProperty("decision_at")
    private String decisionAt;
    @JsonProperty("total_player")
    private int totalPlayer;
    @JsonProperty("items")
    private List<TradeDetailResponse> items;
    @JsonProperty("review_status")
    private int reviewStatus;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getReviewDeadline() {
        return reviewDeadline;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public Integer getRound() {
        return round;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public Integer getWithTeamId() {
        return withTeamId;
    }

    public TeamResponse getWithTeam() {
        return withTeam;
    }

    public Integer getTotalReview() {
        return totalReview;
    }

    public Integer getTotalApproval() {
        return totalApproval;
    }

    public Integer getTotalRejection() {
        return totalRejection;
    }

    public Integer getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public String getDecisionAt() {
        return decisionAt;
    }

    public int getTotalPlayer() {
        return totalPlayer;
    }

    public List<TradeDetailResponse> getItems() {
        return items;
    }

    public int getReviewStatus() {
        return reviewStatus;
    }
}
