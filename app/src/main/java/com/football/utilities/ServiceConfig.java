package com.football.utilities;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface ServiceConfig {
    // base url
    String BASE_URL = "http://192.168.1.150/api/v1/";
//    String BASE_URL = "http://52.77.241.109/api/v1/";
    String CONTACT = BASE_URL + "admin/posts/frontcms/contact";
    String FAQ = BASE_URL + "admin/posts/frontcms/faq";
    String TERMS = BASE_URL + "admin/posts/frontcms/terms_conditions";
    String PRIVACY_POLICY = BASE_URL + "admin/posts/frontcms/privacy_policy";

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

    // auth
    String REGISTER = "auth/register";
    String LOGIN = "login";
    String LOGIN_SOCIAL = "auth/social";
    String LOGOUT = "users/logout";
    String FORGOT_PASSWORD = "users/forgot";

    // file
    String UPLOAD = "upload";

    // invitation
    String SEARCH_FRIEND = "invitations/search_friends";
    String INVITE_FRIEND = "invitations";
    String INVITATION_DECISION = "invitations/decision/{id}";
    String PENDING_INVITATIONS = "invitations/pending_list";

    // league
    String SEASONS = "seasons";
    String FORM_OPTIONS = "leagues/get_form/{id}";
    String CREATE_LEAGUE = "leagues";
    String UPDATE_LEAGUE = "leagues/{id}";
    String OPEN_LEAGUES = "leagues/open_league";
    String JOINT = "leagues/join/{id}";
    String MY_LEAGUES = "leagues/my_index";
    String LEAGUE_DETAIL = "leagues/{id}";
    String LEAVE_LEAGUES = "leagues/leave/{id}";
    String REMOVE_TEAM = "leagues/remove_team/{" + KEY_ID + "}/{" + KEY_TEAM_ID + "}";
    String START_LEAGUE = "leagues/{id}/start";
    String STOP_LEAGUE = "leagues/{id}";
    String MATCH_RESULTS = "leagues/get_match_results";
    String MY_MATCH_RESULTS = "leagues/get_my_match_results";
    String TEAM_RESULTS = "leagues/{id}/get_team_results";

    // player
    String REAL_CLUB = "real_clubs";
    String PLAYER_LIST = "players";
    String PLAYER_DETAIL = "players/{id}";
    String PLAYER_STATISTIC = "players/{id}/statistic";
    String PLAYER_STATISTIC_WITH_TEAM = "players/{player_id}/team/{team_id}/statistic";

    // posts
    String HOME_NEWS = "posts";

    // real match
    String REAL_MATCHES = "real_rounds";

    // teams
    String TEAM_LIST = "teams";
    String CREATE_TEAM = "teams";
    String UPDATE_TEAM = "teams/{id}";
    String TEAM_DETAIL = "teams/{id}";
    String TEAM_LINEUP = "teams/lineup/{id}";
    String ADD_PLAYER = "teams/add_player";
    String REMOVE_PLAYER = "teams/remove_player";
    String TEAM_TRANSFERRING = "teams/{id}/transfer_player_list";
    String TRANSFER_PLAYER = "teams/{id}/transfer_player";
    String TRANSFER_HISTORIES = "teams/{id}/transfer_histories";
    String TEAM_STATISTIC = "teams/{id}/statistic";
    String TEAM_SQUAD = "teams/{id}/squad";
    String COMPLETE_LINEUP = "teams/{id}/complete_lineup";
    String CHANGE_TEAM_FORMATION = "teams/{id}/change_formation";
    String TEAM_PITCH_VIEW = "teams/{id}/pitchview";

    // user
    String GET_PROFILE = "profile/{id}";
    String UPDATE_PROFILE = "profile";
    String CHANGE_PASSWORD = "change_password";
    String SETTINGS = "settings";

}
