package com.football.fantasy.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crashlytics.android.Crashlytics;
import com.football.common.activities.BaseAppCompatActivity;
import com.github.nkzawa.socketio.client.Socket;

import io.fabric.sdk.android.Fabric;

public abstract class BaseActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        super.onCreate(savedInstanceState);
    }

    public Socket getSocket() {
        return getAppContext().getSocket();
    }
}
