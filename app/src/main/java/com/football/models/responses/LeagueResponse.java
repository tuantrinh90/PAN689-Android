package com.football.models.responses;

import android.content.Context;

import com.bon.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.fantasy.R;
import com.football.utilities.Constant;

import java.io.Serializable;
import java.util.Calendar;

public class LeagueResponse implements Serializable {
    public static final int WAITING_FOR_START = 1;
    public static final int ON_GOING = 2;
    public static final int FINISHED = 3;

    public static final String LEAGUE_TYPE_OPEN = "open";
    public static final String LEAGUE_TYPE_PRIVATE = "private";

    public static final String SCORING_SYSTEM_REGULAR = "regular";
    public static final String SCORING_SYSTEM_POINTS = "per_stats";

    public static final String GAMEPLAY_OPTION_TRANSFER = "transfer";
    public static final String GAMEPLAY_OPTION_DRAFT = "draft";

    public static final String TRADE_REVIEW_NO_REVIEW = "no";
    public static final String TRADE_REVIEW_CREATOR = "creator";
    public static final String TRADE_REVIEW_MEMBER = "members";

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
    @JsonProperty("gameplay_option")
    private String gameplayOption;
    @JsonProperty("gameplay_option_display")
    private String gameplayOptionDisplay;
    @JsonProperty("season_id")
    private Integer seasonId;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("logo")
    private String logo;
    @JsonProperty("league_type")
    private String leagueType;
    @JsonProperty("league_type_display")
    private String leagueTypeDisplay;
    @JsonProperty("number_of_user")
    private Integer numberOfUser;
    @JsonProperty("current_number_of_user")
    private Integer currentNumberOfUser;
    @JsonProperty("scoring_system")
    private String scoringSystem;
    @JsonProperty("scoring_system_display")
    private String scoringSystemDisplay;
    @JsonProperty("start_at")
    private String startAt;
    @JsonProperty("end_at")
    private String endAt;
    @JsonProperty("real_round_start")
    private Integer realRoundStart;
    @JsonProperty("real_round_end")
    private Integer realRoundEnd;
    @JsonProperty("budget_id")
    private Integer budgetId;
    @JsonProperty("budget_value")
    private Integer budgetValue;
    @JsonProperty("budget_option")
    private BudgetResponse budgetOption;
    @JsonProperty("team_setup")
    private String teamSetup;
    @JsonProperty("trade_review")
    private String tradeReview;
    @JsonProperty("trade_review_display")
    private String tradeReviewDisplay;
    @JsonProperty("draft_time")
    private String draftTime;
    @JsonProperty("time_to_pick")
    private Integer timeToPick;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("status_display")
    private String statusDisplay;
    @JsonProperty("is_owner")
    private Boolean isOwner;
    @JsonProperty("rank")
    private Integer rank;
    @JsonProperty("rank_status")
    private Integer rankStatus;
    @JsonProperty("invitation")
    private InvitationResponse invitation;
    @JsonProperty("is_joined")
    private Boolean isJoined;
    @JsonProperty("team")
    private TeamResponse team;
    @JsonProperty("transfer_deadline")
    private String transferDeadline;
    @JsonProperty("round")
    private int round;

    public LeagueResponse() {
    }

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

    public String getGameplayOption() {
        return gameplayOption;
    }

    public void setGameplayOption(String gameplayOption) {
        this.gameplayOption = gameplayOption;
    }

    public String getGameplayOptionDisplay() {
        return gameplayOptionDisplay + "";
    }

    public void setGameplayOptionDisplay(String gameplayOptionDisplay) {
        this.gameplayOptionDisplay = gameplayOptionDisplay;
    }

    public Integer getSeasonId() {
        return seasonId == null ? 0 : seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
    }

    public String getName() {
        return name + "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description + "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo + "";
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLeagueType() {
        return leagueType + "";
    }

    public void setLeagueType(String leagueType) {
        this.leagueType = leagueType;
    }

    public String getLeagueTypeDisplay() {
        return leagueTypeDisplay + "";
    }

    public void setLeagueTypeDisplay(String leagueTypeDisplay) {
        this.leagueTypeDisplay = leagueTypeDisplay;
    }

    public Integer getNumberOfUser() {
        return numberOfUser == null ? 0 : numberOfUser;
    }

    public void setNumberOfUser(Integer numberOfUser) {
        this.numberOfUser = numberOfUser;
    }

    public Integer getCurrentNumberOfUser() {
        return currentNumberOfUser == null ? 0 : currentNumberOfUser;
    }

    public void setCurrentNumberOfUser(Integer currentNumberOfUser) {
        this.currentNumberOfUser = currentNumberOfUser;
    }

    public String getScoringSystem() {
        return scoringSystem + "";
    }

    public void setScoringSystem(String scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public String getScoringSystemDisplay() {
        return scoringSystemDisplay + "";
    }

    public void setScoringSystemDisplay(String scoringSystemDisplay) {
        this.scoringSystemDisplay = scoringSystemDisplay;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    @JsonIgnore
    public Calendar getStartAtCalendar() {
        return DateTimeUtils.convertStringToCalendar(startAt, Constant.FORMAT_DATE_TIME_SERVER);
    }

    @JsonIgnore
    public Calendar getEndAtCalendar() {
        return DateTimeUtils.convertStringToCalendar(endAt, Constant.FORMAT_DATE_TIME_SERVER);
    }

    @JsonIgnore
    public Calendar getTransferDeadlineCalendar() {
        return DateTimeUtils.convertStringToCalendar(transferDeadline, Constant.FORMAT_DATE_TIME_SERVER);
    }

    public Integer getRealRoundStart() {
        return realRoundStart == null ? 0 : realRoundStart;
    }

    public void setRealRoundStart(Integer realRoundStart) {
        this.realRoundStart = realRoundStart;
    }

    public Integer getRealRoundEnd() {
        return realRoundEnd == null ? 0 : realRoundEnd;
    }

    public void setRealRoundEnd(Integer realRoundEnd) {
        this.realRoundEnd = realRoundEnd;
    }

    public Integer getBudgetId() {
        return budgetId == null ? 0 : budgetId;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public Integer getBudgetValue() {
        return budgetValue == null ? 0 : budgetValue;
    }

    public void setBudgetValue(Integer budgetValue) {
        this.budgetValue = budgetValue;
    }

    public BudgetResponse getBudgetOption() {
        return budgetOption;
    }

    public void setBudgetOption(BudgetResponse budgetOption) {
        this.budgetOption = budgetOption;
    }

    public String getTeamSetup() {
        return teamSetup + "";
    }

    public void setTeamSetup(String teamSetup) {
        this.teamSetup = teamSetup;
    }

    @JsonIgnore
    public Calendar getTeamSetUpCalendar() {
        return DateTimeUtils.convertStringToCalendar(teamSetup, Constant.FORMAT_DATE_TIME_SERVER);
    }

    @JsonIgnore
    public String getDescriptionText(Context context) {
        Calendar calendarCurrent = Calendar.getInstance();
        long time = 0l;
        if (gameplayOption != null) {
            if (gameplayOption.equalsIgnoreCase(Constant.KEY_TRANSFER)) {
                Calendar calendarTimeSetUp = getTeamSetUpCalendar();
                if (calendarTimeSetUp != null) {
                    time = calendarTimeSetUp.getTimeInMillis() - calendarCurrent.getTimeInMillis();
                }
            } else {
                Calendar calendarDraftTime = getDraftTimeCalendar();
                if (calendarDraftTime != null) {
                    time = calendarDraftTime.getTimeInMillis() - calendarCurrent.getTimeInMillis();
                }
            }

            if (time >= 0) {
                String des;
                String result;

                if (gameplayOption.equalsIgnoreCase(Constant.KEY_TRANSFER)) {
                    des = context.getString(R.string.util_team_setup_time);
                } else {
                    des = context.getString(R.string.util_team_draft_time);
                }

                long division = time / 1000;
                result = division + " " + context.getString(R.string.seconds) + " " + des;

                if (division > 60) {
                    long s = division / 60;
                    result = s + " " + context.getString(s > 1 ? R.string.minutes : R.string.minutes_) + " " + des;
                }

                if (division > 60 * 60) {
                    long h = division / 60 / 60;
                    result = h + " " + context.getString(h > 1 ? R.string.hours : R.string.hours_) + " " + des;
                }

                if (division > 60 * 60 * 24) {
                    long d = division / 60 / 60 / 24;
                    result = d + " " + context.getString(d > 1 ? R.string.days : R.string.days_) + " " + des;
                }

                return result;
            }
        }

        return statusDisplay + "";
    }

    public String getTradeReview() {
        return tradeReview + "";
    }

    public void setTradeReview(String tradeReview) {
        this.tradeReview = tradeReview;
    }

    public String getTradeReviewDisplay() {
        return tradeReviewDisplay + "";
    }

    public void setTradeReviewDisplay(String tradeReviewDisplay) {
        this.tradeReviewDisplay = tradeReviewDisplay;
    }

    public String getDraftTime() {
        return draftTime;
    }

    public void setDraftTime(String draftTime) {
        this.draftTime = draftTime;
    }

    @JsonIgnore
    public Calendar getDraftTimeCalendar() {
        return DateTimeUtils.convertStringToCalendar(draftTime, Constant.FORMAT_DATE_TIME_SERVER);
    }

    public Integer getTimeToPick() {
        return timeToPick == null ? 0 : timeToPick;
    }

    public void setTimeToPick(Integer timeToPick) {
        this.timeToPick = timeToPick;
    }

    public Integer getStatus() {
        return status == null ? 0 : status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay + "";
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public Boolean getOwner() {
        return isOwner == null ? false : isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public InvitationResponse getInvitation() {
        return invitation;
    }

    public void setInvitation(InvitationResponse invitation) {
        this.invitation = invitation;
    }

    public Integer getRank() {
        return rank == null ? 0 : rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getRankStatus() {
        return rankStatus == null ? 0 : rankStatus;
    }

    public void setRankStatus(Integer rankStatus) {
        this.rankStatus = rankStatus;
    }

    public Boolean getIsJoined() {
        return isJoined == null ? false : isJoined;
    }

    public void setIsJoined(Boolean isJoined) {
        this.isJoined = isJoined;
    }

    public TeamResponse getTeam() {
        return team;
    }

    public void setTeam(TeamResponse team) {
        this.team = team;
    }

    public Boolean getJoined() {
        return isJoined;
    }

    public String getTransferDeadline() {
        return transferDeadline;
    }

    public int getRound() {
        return round;
    }

    @Override
    public String toString() {
        return "LeagueResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", gameplayOption='" + gameplayOption + '\'' +
                ", gameplayOptionDisplay='" + gameplayOptionDisplay + '\'' +
                ", seasonId=" + seasonId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                ", leagueType='" + leagueType + '\'' +
                ", leagueTypeDisplay='" + leagueTypeDisplay + '\'' +
                ", numberOfUser=" + numberOfUser +
                ", currentNumberOfUser=" + currentNumberOfUser +
                ", scoringSystem='" + scoringSystem + '\'' +
                ", scoringSystemDisplay='" + scoringSystemDisplay + '\'' +
                ", startAt='" + startAt + '\'' +
                ", realRoundStart=" + realRoundStart +
                ", realRoundEnd=" + realRoundEnd +
                ", budgetId=" + budgetId +
                ", budgetValue=" + budgetValue +
                ", budgetOption=" + budgetOption +
                ", teamSetup='" + teamSetup + '\'' +
                ", tradeReview='" + tradeReview + '\'' +
                ", tradeReviewDisplay='" + tradeReviewDisplay + '\'' +
                ", draftTime='" + draftTime + '\'' +
                ", timeToPick=" + timeToPick +
                ", status=" + status +
                ", statusDisplay='" + statusDisplay + '\'' +
                ", isOwner=" + isOwner +
                ", rank=" + rank +
                ", rankStatus=" + rankStatus +
                ", invitation=" + invitation +
                ", isJoined=" + isJoined +
                '}';
    }
}
