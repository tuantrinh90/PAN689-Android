package com.football.interactors.service;

import android.content.Context;
import android.text.TextUtils;

import com.bon.share_preferences.AppPreferences;
import com.football.common.Keys;
import com.football.utilities.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dangpp on 2/9/2018.
 */
public class AccessInterceptor implements Interceptor {
    private String token;

    public AccessInterceptor(Context context) {
        token = AppPreferences.getInstance(context).getString(Constant.KEY_TOKEN);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request;

        // update token
        if (!TextUtils.isEmpty(token)) {
            request = chain.request().newBuilder()
                    .addHeader(Keys.AUTH_TOKEN, "Bearer " + token)
                    .build();
        } else {
            request = chain.request();
        }

        return chain.proceed(request);
    }
}