package com.football.fantasy.fragments.account.signin;


import android.text.TextUtils;
import android.util.Log;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.requests.LoginRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.LocaleHelper;
import com.football.utilities.RxUtilities;

import java.util.Locale;

import okhttp3.MultipartBody;

/**
 * Created by dangpp on 3/1/2018.
 */

public class SignInDataPresenter<V extends ISignInView> extends BaseDataPresenter<V> implements ISignInDataPresenter<V> {

    private static final String TAG = "SignInDataPresenter";

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
                                loginSuccess(userResponse, "");
                            }

                            @Override
                            public void onError(String e) {
                                loginError(e);
                            }
                        }));
            }
        });
    }

    @Override
    public void onSignIn(String provider, String accessToken, String secret) {
        getOptView().doIfPresent(v -> {
            LoginRequest loginRequest = v.getLoginRequest();
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().loginSocial(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("provider", provider)
                            .addFormDataPart("access_token", accessToken)
                            .addFormDataPart("secret_key", secret)
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
                            Log.d(TAG, "onSuccess: " + provider);
                            loginSuccess(userResponse, provider);
                        }

                        @Override
                        public void onError(String e) {
                            Log.d(TAG, "onSuccess: " + provider);
                            loginError(e);
                        }
                    }));
        });
    }

    private void loginSuccess(UserResponse response, String provider) {
        getOptView().doIfPresent(view -> {
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putString(Constant.KEY_TOKEN, response.getApiToken());
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putInt(Constant.KEY_USER_ID, response.getId());
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putString(Constant.KEY_LOGIN_TYPE, provider);

            String lang = !TextUtils.isEmpty(response.getLocale()) ? response.getLocale() : Locale.getDefault().getLanguage();
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putString(Constant.KEY_LANGUAGE, lang);
            LocaleHelper.setLocale(view.getAppActivity(), lang);

            view.goToMain();
        });
    }

    private void loginError(String e) {
        getOptView().doIfPresent(view -> {
            view.showMessage(e, R.string.ok, null);
        });
    }
}
