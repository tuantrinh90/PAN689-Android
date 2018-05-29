package com.football.utilities;

public interface ErrorCodes {
    int UNAUTHORIZED = 401;
    int FORBIDDEN = 403;
    int NOT_FOUND = 404;
    int SERVER_ERROR = 500;
    int BAD_GATEWAY = 502;
    int NO_INTERNET = -1;
    int TIME_OUT = -2;
    int UPDATING = -3;
    int UNKNOWN = 0;
}
