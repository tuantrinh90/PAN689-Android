package com.football.fantasy.fragments.account.forgot;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class ForgotPasswordPresenter extends BaseDataPresenter<IForgotPasswordView> implements IForgotPasswordPresenter<IForgotPasswordView> {
    /**
     * @param appComponent
     */
    protected ForgotPasswordPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
