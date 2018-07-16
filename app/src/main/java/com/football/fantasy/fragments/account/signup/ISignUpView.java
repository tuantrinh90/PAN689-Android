package com.football.fantasy.fragments.account.signup;

import com.football.common.views.IBaseMvpView;

public interface ISignUpView extends IBaseMvpView {
    void onRegisterSuccess();

    void goToMain();
}
