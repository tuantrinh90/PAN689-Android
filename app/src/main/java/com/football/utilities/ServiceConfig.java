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

    String LOGIN = "login";
    String LOGIN_SOCIAL = "auth/social";
    String REGISTER = "auth/store";
    String LOGOUT = "users/logout";
    String FORGOT_PASSWORD = "auth/recover-password";
    String MY_LEAGUES = "leagues/index";
    String PENDING_INVITATIONS = "invitations/pending_list";
    String INVITE_FRIEND ="invitations/search_friends";
    String INVITATION_DECISION ="invitations/decision/{id}";
    String HOME_NEWS = "posts";
}
