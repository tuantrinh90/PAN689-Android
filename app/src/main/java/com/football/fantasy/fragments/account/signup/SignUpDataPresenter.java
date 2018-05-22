package com.football.fantasy.fragments.account.signup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class SignUpDataPresenter<V extends ISignUpView> extends BaseDataPresenter<V> implements ISignUpPresenter<V> {
    /**
     * @param appComponent
     */
    protected SignUpDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
