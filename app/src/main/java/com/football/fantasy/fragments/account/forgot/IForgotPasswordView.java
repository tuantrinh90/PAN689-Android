package com.football.fantasy.fragments.account.forgot;

import com.football.common.views.IBaseMvpView;

public interface IForgotPasswordView extends IBaseMvpView {
    void onSuccess(String email);
}
