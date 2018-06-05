package com.football.models.requests;

public class TeamRequest {
    int leagueId;
    String name = "";
    String description = "";
    String logo = "";

    public TeamRequest(int leagueId, String name, String description, String logo) {
        this.leagueId = leagueId;
        this.name = name;
        this.description = description;
        this.logo = logo;
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

    @Override
    public String toString() {
        return "TeamRequest{" +
                "leagueId=" + leagueId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
