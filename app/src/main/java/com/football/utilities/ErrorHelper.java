package com.football.utilities;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bon.util.StringUtils;
import com.football.application.AppContext;
import com.football.common.views.IBaseMvpView;
import com.football.events.UnauthorizedEvent;
import com.football.fantasy.R;
import com.football.models.ErrorBody;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class ErrorHelper {
    public static String getBaseErrorText(@NonNull IBaseMvpView mvpView, @NonNull ErrorBody error) {
        Context context = AppContext.getInstance();
        int code = error.getStatus();
        String stringRes;

        if (code >= ErrorCodes.SERVER_ERROR && code < 600) {
            return context.getString(R.string.some_thing_wrong);
        }

        if (ConnectionHelper.isOfflineMode(code)) {
            return context.getString(R.string.you_are_offline);
        }

        switch (code) {
            case ErrorCodes.NO_INTERNET:
                stringRes = context.getString(R.string.no_internet);
                break;
            case ErrorCodes.TIME_OUT:
                stringRes = context.getString(R.string.server_not_responding);
                break;
            case ErrorCodes.FORBIDDEN:
                stringRes = context.getString(R.string.forbidden);
                break;
            case ErrorCodes.APP_ERROR:
                stringRes = context.getString(context.getResources().getIdentifier(error.getMessage(), "string", context.getPackageName()));
                if (StringUtils.isEmpty(stringRes))
                    stringRes = context.getString(R.string.not_found_resource);
                break;
            case ErrorCodes.UNAUTHORIZED:
                stringRes = context.getString(R.string.unauthorized);
                mvpView.getRxBus().send(new UnauthorizedEvent(stringRes));
                break;
            case ErrorCodes.NOT_FOUND:
                stringRes = context.getString(R.string.server_not_found);
                break;
            default:
                stringRes = context.getString(R.string.unknown_error);
                break;
        }

        return stringRes;
    }

    @NonNull
    public static ErrorBody createErrorBody(Throwable e) {
        System.out.println("ErrorBody::CreateErrorBody::" + e.getMessage());
        ErrorBody error = new ErrorBody();
        if (e instanceof HttpException) {
            error.setStatus(((HttpException) e).code());
            error.setMessage(((HttpException) e).message());
            if (((HttpException) e).response() != null && ((HttpException) e).response().errorBody() != null) {
                try {
                    String stringErrorBody = ((HttpException) e).response().errorBody().string();
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

        System.out.println("ErrorBody::ErrorBody::" + error.toString());
        return error;
    }

    /**
     * @param messageCode
     * @return
     */
    public static ErrorBody getErrorBodyApp(String messageCode) {
        System.out.println("ErrorBody::GetErrorBodyApp::" + messageCode);
        return new ErrorBody(ErrorCodes.APP_ERROR, messageCode);
    }
}
