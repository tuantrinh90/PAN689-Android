package com.football.utilities;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface ServiceConfig {
    // base url
    String BASE_URL = "http://52.77.241.109/api/v1/";

    // retry policy
    boolean RETRY_POLICY = true;

    // timeout
    int REQUEST_FILE_TIMEOUT = 100;
    int REQUEST_TIMEOUT_LONG = 60;
    int REQUEST_TIMEOUT = 30;

    String PROVIDER_FACEBOOK = "facebook";
    String PROVIDER_GOOGLE = "google";
    String PROVIDER_TWITTER = "twitter";

    String KEY_ID = "id";
    String KEY_TEAM_ID = "team_id";
    String KEY_LEAGUE_ID = "league_id";

    String LOGIN = "login";
    String LOGIN_SOCIAL = "auth/social";
    String REGISTER = "auth/register";
    String RECOVER_PASSWORD = "users/forgot";
    String LOGOUT = "users/logout";
    String FORGOT_PASSWORD = "users/forgot";
    String MY_LEAGUES = "leagues/my_index";
    String PENDING_INVITATIONS = "invitations/pending_list";
    String LEAGUE = "leagues/{id}";
    String TEAMS = "teams";
    String TEAM_DETAILS = "teams/{id}";
    String TEAM_SQUAD = "teams/{id}/squad";
    String TEAM_STATISTIC = "teams/{id}/statistic";
    String TEAM_LINEUP = "teams/lineup/{id}";
    String TEAM_PITCH_VIEW = "teams/{id}/pitchview";
    String REMOVE_TEAM = "leagues/remove_team/{" + KEY_ID + "}/{" + KEY_TEAM_ID + "}";
    String INVITE_FRIEND = "invitations";
    String STOP_LEAGUE = "leagues/{id}";
    String SEARCH_FRIEND = "invitations/search_friends";
    String INVITATION_DECISION = "invitations/decision/{id}";
    String HOME_NEWS = "posts";
    String OPEN_LEAGUES = "leagues/open_league";
    String JOINT = "leagues/join/{id}";
    String PLAYER_LIST = "players"; //     String PLAYER_LIST = "teams/player_list/{" + KEY_ID + "}";
    String REAL_CLUB = "real_clubs";
    String SEASONS = "seasons";
    String CREATE_LEAGUE = "leagues";
    String UPDATE_LEAGUE = "leagues/{" + KEY_ID + "}";
    String FORM_OPTIONS = "leagues/get_form/{" + KEY_ID + "}";
    String CREATE_TEAM = "teams";
    String UPDATE_TEAM = "teams/{id}";
    String LINEUP = "teams/lineup/{" + KEY_ID + "}";
    String ADD_PLAYER = "teams/add_player";
    String REMOVE_PLAYER = "teams/remove_player";
    String PLAYERS_STATISTIC = "players/{id}/statistic";

    String LEAVE_LEAGUES = "leagues/leave/{id}";
    String UPLOAD = "upload";
}
