package com.football.models.requests;

public class LeagueRequest {

    private int leagueId;
    private String name = "";
    private String logo = "";
    private String leagueType = "";
    private String gameplayOption = "";
    private int numberOfUser;
    private int budgetId;
    private String tradeReview = "";
    private String scoringSystem = "";
    private String draftTime = "";
    private int timeToPick;
    private String startAt = "";
    private String description = "";
    private String teamSetup = "";

    public LeagueRequest() {
    }

    public int getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(int leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getNumberOfUser() {
        return numberOfUser;
    }

    public void setNumberOfUser(int numberOfUser) {
        this.numberOfUser = numberOfUser;
    }

    public String getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(String scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGameplayOption() {
        return gameplayOption;
    }

    public void setGameplayOption(String gameplayOption) {
        this.gameplayOption = gameplayOption;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
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

    public String getDraftTime() {
        return draftTime;
    }

    public void setDraftTime(String draftTime) {
        this.draftTime = draftTime;
    }

    public int getTimeToPick() {
        return timeToPick;
    }

    public void setTimeToPick(int timeToPick) {
        this.timeToPick = timeToPick;
    }

    @Override
    public String toString() {
        return "LeagueRequest{" +
                "name='" + name + '\'' +
                ", logo='" + logo + '\'' +
                ", leagueType='" + leagueType + '\'' +
                ", numberOfUser=" + numberOfUser +
                ", scoringSystem='" + scoringSystem + '\'' +
                ", startAt='" + startAt + '\'' +
                ", description='" + description + '\'' +
                ", gameplayOption='" + gameplayOption + '\'' +
                ", budgetId=" + budgetId +
                ", teamSetup='" + teamSetup + '\'' +
                ", tradeReview='" + tradeReview + '\'' +
                ", draftTime='" + draftTime + '\'' +
                ", timeToPick='" + timeToPick + '\'' +
                '}';
    }
}
