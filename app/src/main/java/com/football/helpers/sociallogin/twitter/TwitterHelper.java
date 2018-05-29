package com.football.helpers.sociallogin.twitter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class TwitterHelper {
    private TwitterAuthClient mAuthClient;

    @NonNull
    private final Activity mActivity;

    @NonNull
    private final TwitterListener mListener;

    public TwitterHelper(@NonNull TwitterListener response, @NonNull Activity context,
                         @NonNull String twitterApiKey, @NonNull String twitterSecreteKey) {
        mActivity = context;
        mListener = response;

        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterApiKey, twitterSecreteKey);
        TwitterConfig config = new TwitterConfig.Builder(mActivity.getApplicationContext())
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(authConfig)
                .debug(true)
                .build();
        Twitter.initialize(config);

        mAuthClient = new TwitterAuthClient();
    }

    private Callback<TwitterSession> mCallback = new Callback<TwitterSession>() {
        @Override
        public void success(Result<TwitterSession> result) {
            TwitterSession session = result.data;
            mListener.onTwitterSignIn(session.getAuthToken().token, session.getAuthToken().secret,
                    session.getUserId());
        }

        @Override
        public void failure(TwitterException exception) {
            mListener.onTwitterError(exception.getMessage());
        }
    };

    public void performSignIn() {
        mAuthClient.authorize(mActivity, mCallback);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mAuthClient != null) mAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}