package com.football.utilities;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.Log;

import com.bon.util.StringUtils;
import com.football.fantasy.R;
import com.football.models.ErrorBody;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ErrorHelper {
    @StringRes
    public static int getBaseErrorText(@NonNull ErrorBody error) {
        int code = error.getStatus();
        int stringRes;
        if (code >= ErrorCodes.SERVER_ERROR && code < 600) {
            return R.string.some_thing_wrong;
        }

        if (ConnectionHelper.isOfflineMode(code)) {
            return R.string.you_are_offline;
        }

        switch (code) {
            case ErrorCodes.NO_INTERNET:
                stringRes = R.string.no_internet;
                break;
            case ErrorCodes.TIME_OUT:
                stringRes = R.string.server_not_responding;
                break;
            case ErrorCodes.FORBIDDEN:
                stringRes = R.string.forbidden;
                break;
            default:
                stringRes = R.string.unknown_error;
                break;
        }

        return stringRes;
    }

    @NonNull
    public static ErrorBody createErrorBody(Throwable e) {
        ErrorBody error = new ErrorBody();
        if (e instanceof HttpException) {
            error.setStatus(((HttpException) e).code());
            error.setMessage(((HttpException) e).message());

            if (((HttpException) e).response() != null && ((HttpException) e).response().errorBody() != null) {
                try {
                    String stringErrorBody = ((HttpException) e).response().errorBody().string();
                    Log.e("stringErrorBody", "stringErrorBody:: " + stringErrorBody);
                    StringUtils.isNotEmpty(stringErrorBody, s -> error.setMessage(s));
                } catch (IOException | JsonParseException e1) {
                    e.printStackTrace();
                }
            }
        } else if (e instanceof IOException) {
            error.setMessage(e.getMessage());
            error.setStatus(e instanceof UnknownHostException ? ErrorCodes.NO_INTERNET : ErrorCodes.TIME_OUT);
        } else {
            error.setStatus(ErrorCodes.UNKNOWN);
        }
        return error;
    }
}
