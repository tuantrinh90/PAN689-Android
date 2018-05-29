package com.football.utilities;

/**
 * Created by dangpp on 2/9/2018.
 */

public interface ServiceConfig {
    // base url
    String BASE_URL = "https://google.com.vn";

    // retry policy
    boolean RETRY_POLICY = true;

    // timeout
    int REQUEST_FILE_TIMEOUT = 100;
    int REQUEST_TIMEOUT_LONG = 60;
    int REQUEST_TIMEOUT = 30;
}
