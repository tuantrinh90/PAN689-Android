package com.football.utilities;

public interface Constant {
    int NETWORK_STATUS_CODE_SUCCESS = 200;
    String FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm";
    String FORMAT_DATE_TIME_SERVER = "yyyy-MM-dd HH:mm:ss";

    // login response
    String KEY_USER = "key_user";
    float KEY_MIO = 1000000f;
    String KEY_ORDER_BY = "orderBy";
    String KEY_PAGE = "page";
    String KEY_PER_PAGE = "per_page";
    String KEY_TRANSFER = "transfer";
    String KEY_DRAFT = "draft";
    int KEY_INVITATION_DECLINE = -1;
    int KEY_INVITATION_ACCEPT = 1;
}
