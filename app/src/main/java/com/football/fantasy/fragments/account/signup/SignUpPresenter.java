package com.football.fantasy.fragments.account.signup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class SignUpPresenter<V extends ISignUpView> extends BaseDataPresenter<V> implements ISignUpPresenter<V> {
    /**
     * @param appComponent
     */
    protected SignUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
