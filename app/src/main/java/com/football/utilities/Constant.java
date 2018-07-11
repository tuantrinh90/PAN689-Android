package com.football.utilities;

public interface Constant {
    int NETWORK_STATUS_CODE_SUCCESS = 200;
    String FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm";
    String FORMAT_DATE_TIME_SERVER = "yyyy-MM-dd HH:mm:ss";
    String FORMAT_DATE = "yyyy/MM/dd";
    String FORMAT_DATE_SERVER = "yyyy-MM-dd";
    String FORMAT_DAY_OF_WEEK = "EEEE";
    String FORMAT_HOUR_MINUTE = "HH:mm";

    // login response
    String KEY_USER = "key_user";
    float KEY_MIO = 1000000f;
    String KEY_LEAGUE_ID = "league_id";
    String KEY_SORT = "sort";
    String KEY_PAGE = "page";
    String KEY_PER_PAGE = "per_page";
    String KEY_TRANSFER = "transfer";
    String KEY_DRAFT = "draft";
    String KEY_WORD = "keyword";
    String NUMBER_OF_USER = "number_of_user";
    String NUMBER_OF_USER_ALL = "0";
    String KEY_MAIN_POSITION = "main_position";
    String KEY_CLUBS = "real_club_id";
    String KEY_SEASON = "season_id";
    int KEY_INVITATION_DECLINE = -1;
    int KEY_INVITATION_ACCEPT = 1;
    int MIN_PASSWORD = 6;
    int MAX_NAME_40_CHARACTERS = 40;
    int MAX_255_CHARACTERS = 255;
    int MIN_PHONE = 9;
    int MAX_PHONE = 12;

    int SORT_NONE = -1;
    int SORT_DESC = 0;
    int SORT_ASC = 1;

    int PAGE_START_INDEX = 1;

}
