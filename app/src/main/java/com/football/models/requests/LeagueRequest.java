package com.football.models.requests;

public class LeagueRequest {

    public static final String LEAGUE_TYPE_OPEN = "open";
    public static final String LEAGUE_TYPE_PRIVATE = "private";

    public static final String SCORING_SYSTEM_REGULAR = "regular";
    public static final String SCORING_SYSTEM_POINTS = "points";

    public static final String GAMEPLAY_OPTION_TRANSFER = "transfer";
    public static final String GAMEPLAY_OPTION_DRAFT = "draft";

    public String name = "";
    public String logo = "";
    public String league_type = "";
    public int number_of_user;
    public String scoring_system = "";
    public String start_at = "";
    public String description = "";
    public String gameplay_option = "";
    public int budget_id;
    public String team_setup = "";
    public String trade_review = "";
    public String draft_time = "";
    public String time_to_pick = "";

}
