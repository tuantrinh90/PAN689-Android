package com.football.interactors.service;

import android.content.Context;
import android.util.Log;

import com.bon.share_preferences.AppPreferences;
import com.bon.util.StringUtils;
import com.football.common.Keys;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dangpp on 2/9/2018.
 */
public class AccessInterceptor implements Interceptor {
    private UserResponse userResponse;

    public AccessInterceptor(Context context) {
        userResponse = AppPreferences.getInstance(context).getObject(Constant.KEY_USER, UserResponse.class);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;

        // update token
        if (userResponse != null && !StringUtils.isEmpty(userResponse.getApiToken())) {
            request = chain.request().newBuilder()
                    .addHeader(Keys.AUTH_TOKEN, "Bearer " + userResponse.getApiToken())
                    .build();
        } else {
            request = chain.request();
        }

        return chain.proceed(request);
    }
}