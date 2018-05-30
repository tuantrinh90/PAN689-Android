package com.football.fantasy.fragments.account.signin;


import com.bon.share_preferences.AppPreferences;
import com.football.application.AppContext;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.requests.LoginRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

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
                LoginRequest loginRequest = v.getLoginRequest();
                mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().loginService(new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("email", loginRequest.getEmail())
                                .addFormDataPart("password", loginRequest.getPassword())
                                .addFormDataPart("device_token", loginRequest.getDeviceToken())
                                .build()),
                        new ApiCallback<UserResponse>() {
                            @Override
                            public void onStart() {
                                v.showLoading(true);
                            }

                            @Override
                            public void onComplete() {
                                v.showLoading(false);
                            }

                            @Override
                            public void onSuccess(UserResponse userResponse) {
                                AppPreferences.getInstance(AppContext.getInstance()).putObject(Constant.KEY_USER, userResponse);
                                v.goToMain();
                            }

                            @Override
                            public void onError(String e) {
                                v.showMessage(e, R.string.ok, null);
                            }
                        }));
            }
        });
    }
}
