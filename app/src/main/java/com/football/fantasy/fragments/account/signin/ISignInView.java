package com.football.fantasy.fragments.account.signin;

import android.content.Intent;

import com.football.common.views.IBaseMvpView;
import com.football.models.requests.LoginRequest;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface ISignInView extends IBaseMvpView {
    boolean isValid();

    void goToMain();

    LoginRequest getLoginRequest();

    void onActivityResults(int requestCode, int resultCode, Intent data);
}
