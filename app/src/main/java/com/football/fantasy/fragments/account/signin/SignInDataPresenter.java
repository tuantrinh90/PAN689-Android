package com.football.fantasy.fragments.account.signin;


import android.util.Log;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
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
    }

    @Override
    public void onSignIn() {
        getOptView().doIfPresent(v -> {
            if (v.isValid()) {
                v.showLoading(true);
                LoginRequest loginRequest = v.getLoginRequest();
                mCompositeDisposable.add(RxUtilities.async(
                        v,
                        dataModule.getApiService().login(
                                loginRequest.getEmail(),
                                loginRequest.getPassword(),
                                loginRequest.getDeviceToken()),
                        new ApiCallback<UserResponse>() {
                            @Override
                            public void onSuccess(UserResponse userResponse) {
                                Log.e("TAG", userResponse.toString());
                                loginSuccess(userResponse);
                            }

                            @Override
                            public void onError(String e) {
                                Log.e("eee", e);
                                loginError(e);
                            }
                        }));
            }
        });
    }

    @Override
    public void onSignIn(String provider, String accessToken) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().loginSocial(provider, accessToken, ""),
                    new ApiCallback<UserResponse>() {
                        @Override
                        public void onSuccess(UserResponse userResponse) {
                            Log.e("TAG", userResponse.toString());
                            loginSuccess(userResponse);
                        }

                        @Override
                        public void onError(String e) {
                            Log.e("eee", e);
                            loginError(e);
                        }
                    }));
        });
    }

    private void loginSuccess(UserResponse response) {
        getOptView().doIfPresent(view -> {
            view.goToMain();
            view.showLoading(false);
        });
    }

    private void loginError(String e) {
        // TODO: 5/30/2018 fake login
        getOptView().doIfPresent(view -> {
            view.showMessage("Fake login nhé");
            view.goToMain();
            view.showLoading(false);
        });
    }
}
