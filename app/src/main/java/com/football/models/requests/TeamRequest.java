package com.football.models.requests;

public class TeamRequest {

    public int league_id;
    public String name;
    public String description;
    public String logo;

    public TeamRequest(int league_id, String name, String description, String logo) {
        this.league_id = league_id;
        this.name = name;
        this.description = description;
        this.logo = logo;
    }
}
