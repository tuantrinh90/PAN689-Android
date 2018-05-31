package com.football.fantasy.fragments.account.forgot;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IForgotPasswordPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void recoverPassword(String email);

}
