package com.football.fantasy.fragments.account.forgot_success;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.fragments.account.forgot.IForgotPasswordPresenter;

public class ForgotPasswordSuccessPresenter extends BaseDataPresenter<IForgotPasswordSuccessView> implements IForgotPasswordSuccessPresenter<IForgotPasswordSuccessView> {
    /**
     * @param appComponent
     */
    protected ForgotPasswordSuccessPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
