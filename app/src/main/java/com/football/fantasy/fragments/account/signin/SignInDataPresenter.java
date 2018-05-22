package com.football.fantasy.fragments.account.signin;


import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.events.SignInEvent;

/**
 * Created by dangpp on 3/1/2018.
 */

public class SignInDataPresenter<V extends ISignInView> extends BaseDataPresenter<V> implements ISignInDataPresenter<V> {
    /**
     * @param appComponent
     */
    protected SignInDataPresenter(AppComponent appComponent) {
        super(appComponent);
        bus.subscribe(this, SignInEvent.class, signInEvent -> {

        });
    }

    @Override
    public void onSignIn() {
        getOptView().doIfPresent(v -> {
            if (v.isValid()) {
                v.goToMain();
            }
        });
    }
}
