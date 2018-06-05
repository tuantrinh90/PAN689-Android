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
    public static final int TYPE_INCREASE = 1;
    public static final int TYPE_DECREASE = 2;

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
    @JsonProperty("gameplayOption")
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
    @JsonProperty("leagueType")
    private String leagueType;
    @JsonProperty("league_type_display")
    private String leagueTypeDisplay;
    @JsonProperty("numberOfUser")
    private Integer numberOfUser;
    @JsonProperty("current_number_of_user")
    private Integer currentNumberOfUser;
    @JsonProperty("scoringSystem")
    private String scoringSystem;
    @JsonProperty("scoring_system_display")
    private String scoringSystemDisplay;
    @JsonProperty("startAt")
    private String startAt;
    @JsonProperty("real_round_start")
    private Integer realRoundStart;
    @JsonProperty("real_round_end")
    private Integer realRoundEnd;
    @JsonProperty("budgetId")
    private Integer budgetId;
    @JsonProperty("budget_value")
    private Integer budgetValue;
    @JsonProperty("budget_option")
    private BudgetResponse budgetOption;
    @JsonProperty("teamSetup")
    private String teamSetup;
    @JsonProperty("tradeReview")
    private String tradeReview;
    @JsonProperty("trade_review_display")
    private String tradeReviewDisplay;
    @JsonProperty("draftTime")
    private String draftTime;
    @JsonProperty("timeToPick")
    private Integer timeToPick;
    @JsonProperty("status")
    private String status;
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
        return gameplayOptionDisplay;
    }

    public void setGameplayOptionDisplay(String gameplayOptionDisplay) {
        this.gameplayOptionDisplay = gameplayOptionDisplay;
    }

    public Integer getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(Integer seasonId) {
        this.seasonId = seasonId;
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

    public String getLeagueType() {
        return leagueType;
    }

    public void setLeagueType(String leagueType) {
        this.leagueType = leagueType;
    }

    public String getLeagueTypeDisplay() {
        return leagueTypeDisplay;
    }

    public void setLeagueTypeDisplay(String leagueTypeDisplay) {
        this.leagueTypeDisplay = leagueTypeDisplay;
    }

    public Integer getNumberOfUser() {
        return numberOfUser;
    }

    public void setNumberOfUser(Integer numberOfUser) {
        this.numberOfUser = numberOfUser;
    }

    public Integer getCurrentNumberOfUser() {
        return currentNumberOfUser;
    }

    public void setCurrentNumberOfUser(Integer currentNumberOfUser) {
        this.currentNumberOfUser = currentNumberOfUser;
    }

    public String getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(String scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public String getScoringSystemDisplay() {
        return scoringSystemDisplay;
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

    public Integer getRealRoundStart() {
        return realRoundStart;
    }

    public void setRealRoundStart(Integer realRoundStart) {
        this.realRoundStart = realRoundStart;
    }

    public Integer getRealRoundEnd() {
        return realRoundEnd;
    }

    public void setRealRoundEnd(Integer realRoundEnd) {
        this.realRoundEnd = realRoundEnd;
    }

    public Integer getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Integer budgetId) {
        this.budgetId = budgetId;
    }

    public Integer getBudgetValue() {
        return budgetValue;
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
        return teamSetup;
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
            result = division + " " + context.getString(R.string.second) + " " + des;

            if (division > 60) {
                result = division / 60 + " " + context.getString(R.string.minute) + " " + des;
            }

            if (division > 60 * 60) {
                result = division / 60 / 60 + " " + context.getString(R.string.hour) + " " + des;
            }

            if (division > 60 * 60 * 24) {
                result = division / 60 / 60 / 24 + " " + context.getString(R.string.day) + " " + des;
            }

            return result;
        } else {
            return "";
        }
    }

    public String getTradeReview() {
        return tradeReview;
    }

    public void setTradeReview(String tradeReview) {
        this.tradeReview = tradeReview;
    }

    public String getTradeReviewDisplay() {
        return tradeReviewDisplay;
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
        return timeToPick;
    }

    public void setTimeToPick(Integer timeToPick) {
        this.timeToPick = timeToPick;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public Boolean getOwner() {
        return isOwner;
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
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getRankStatus() {
        return rankStatus;
    }

    public void setRankStatus(Integer rankStatus) {
        this.rankStatus = rankStatus;
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
                ", status='" + status + '\'' +
                ", statusDisplay='" + statusDisplay + '\'' +
                ", isOwner=" + isOwner +
                ", rank=" + rank +
                ", rankStatus=" + rankStatus +
                ", invitation=" + invitation +
                '}';
    }
}
