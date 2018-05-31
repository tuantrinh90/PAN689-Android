package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LeagueResponse implements Serializable {
    public static final int OPEN_LEAGUES = 1;
    public static final int MY_LEAGUES = 2;
    public static final int PENDING_LEAGUES = 3;

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
    private String status;
    @JsonProperty("status_display")
    private String statusDisplay;
    @JsonProperty("is_owner")
    private Boolean isOwner;

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
                ", budgetOption='" + budgetOption + '\'' +
                ", teamSetup='" + teamSetup + '\'' +
                ", tradeReview='" + tradeReview + '\'' +
                ", tradeReviewDisplay='" + tradeReviewDisplay + '\'' +
                ", draftTime='" + draftTime + '\'' +
                ", timeToPick=" + timeToPick +
                ", status='" + status + '\'' +
                ", statusDisplay='" + statusDisplay + '\'' +
                ", isOwner=" + isOwner +
                '}';
    }
}
