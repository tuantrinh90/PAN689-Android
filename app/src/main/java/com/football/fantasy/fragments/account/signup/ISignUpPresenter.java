package com.football.fantasy.fragments.account.signup;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;
import com.football.models.requests.SignupRequest;

public interface ISignUpPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void register(SignupRequest request);

}
