package com.football.fantasy.fragments.account.signin;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface ISignInPresenter<V extends MvpView> extends MvpPresenter<V> {
    boolean isValid();

    void onSignIn();
}
