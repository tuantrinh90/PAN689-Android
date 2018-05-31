package com.football.fantasy.fragments.account.forgot_success;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IForgotPasswordSuccessPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void forgotPassword(String email);
}
