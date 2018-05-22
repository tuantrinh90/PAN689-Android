package com.football.fantasy.fragments.account.signin;

import com.football.common.views.IBaseMvpView;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface ISignInView extends IBaseMvpView {
    boolean isValid();

    void goToMain();
}
