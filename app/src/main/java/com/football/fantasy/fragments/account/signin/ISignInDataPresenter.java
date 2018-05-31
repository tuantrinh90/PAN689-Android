package com.football.fantasy.fragments.account.signin;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface ISignInDataPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void onSignIn();

    void onSignIn(String provider, String accessToken, String secret);
}
