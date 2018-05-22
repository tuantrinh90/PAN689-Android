package com.football.fantasy.fragments.account.forgot;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class ForgotPasswordDataPresenter extends BaseDataPresenter<IForgotPasswordView> implements IForgotPasswordPresenter<IForgotPasswordView> {
    /**
     * @param appComponent
     */
    protected ForgotPasswordDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
