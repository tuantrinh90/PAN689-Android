package com.football.fantasy.fragments.account.signin;


import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.events.SignInEvent;

/**
 * Created by dangpp on 3/1/2018.
 */

public class SignInPresenter<V extends ISignInView> extends BaseDataPresenter<V> implements ISignInPresenter<V> {
    /**
     * @param appComponent
     */
    protected SignInPresenter(AppComponent appComponent) {
        super(appComponent);
        bus.subscribe(this, SignInEvent.class, signInEvent -> {

        });
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public void onSignIn() {

    }
}
