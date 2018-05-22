package com.football.fantasy.fragments.account.forgot_success;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class ForgotPasswordSuccessDataPresenter extends BaseDataPresenter<IForgotPasswordSuccessView> implements IForgotPasswordSuccessPresenter<IForgotPasswordSuccessView> {
    /**
     * @param appComponent
     */
    protected ForgotPasswordSuccessDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
