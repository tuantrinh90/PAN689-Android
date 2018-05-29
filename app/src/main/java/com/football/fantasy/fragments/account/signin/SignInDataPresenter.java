package com.football.fantasy.fragments.account.signin;


import android.util.Log;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.events.SignInEvent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.LoginRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.RxUtilities;

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
                LoginRequest loginRequest = v.getLoginRequest();
                mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().loginService(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getDeviceToken()),
                        new ApiCallback<UserResponse>() {
                            @Override
                            public void onStart() {
                                super.onStart();
                                v.showLoading(true);
                            }

                            @Override
                            public void onComplete() {
                                super.onComplete();
                                v.showLoading(false);
                            }

                            @Override
                            public void onSuccess(UserResponse userResponse) {
                                Log.e("TAG", userResponse.toString());
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("eee", e.getMessage());
                            }
                        }));
            }
        });
    }
}
