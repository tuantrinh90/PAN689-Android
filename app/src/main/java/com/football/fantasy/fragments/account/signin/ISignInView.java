package com.football.fantasy.fragments.account.signin;

import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by dangpp on 3/1/2018.
 */

public interface ISignInView extends MvpView {
    void showLoading(boolean isLoading);

    void goToMain();
}
