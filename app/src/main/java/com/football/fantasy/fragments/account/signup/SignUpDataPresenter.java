package com.football.fantasy.fragments.account.signup;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.requests.SignupRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.LocaleHelper;
import com.football.utilities.RxUtilities;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MultipartBody;

public class SignUpDataPresenter<V extends ISignUpView> extends BaseDataPresenter<V> implements ISignUpPresenter<V> {
    /**
     * @param appComponent
     */
    protected SignUpDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void register(SignupRequest request) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().register(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("first_name", request.getFirstName())
                            .addFormDataPart("last_name", request.getLastName())
                            .addFormDataPart("email", request.getEmail())
                            .addFormDataPart("password", request.getPassword())
                            .addFormDataPart("password_confirmation", request.getPasswordConfirm())
                            .addFormDataPart("code", request.getCode())
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
                        public void onSuccess(UserResponse response) {
                            v.onRegisterSuccess();
                            v.showMessage(R.string.register_successful, R.string.ok, aVoid -> {
                                autoLogin(request.getEmail(), request.getPassword());
                            });
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e, R.string.ok, null);
                        }
                    }));
        });
    }

    private static final String TAG = "SignUpDataPresenter";
    private int counter = 300;
    private StringBuilder tokenBuilder = new StringBuilder();

    @Override
    public void register300(SignupRequest request) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().register(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("first_name", request.getFirstName())
                            .addFormDataPart("last_name", request.getLastName())
                            .addFormDataPart("email", UUID.randomUUID().toString() + "@yopmail.com")
                            .addFormDataPart("password", request.getPassword())
                            .addFormDataPart("password_confirmation", request.getPasswordConfirm())
                            .addFormDataPart("code", request.getCode())
                            .build()),
                    new ApiCallback<UserResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(UserResponse response) {
                            tokenBuilder.append(response.getApiToken()).append(",");
                            counter--;
                            if (counter > 0) {
                                register300(request);
                                register300(request);
                                Log.d(TAG, "onSuccess: " + counter);
                            } else {
                                writeToFile(tokenBuilder.toString(), v.getAppActivity().getAppContext());
                                v.showLoading(false);
                            }
                        }

                        @Override
                        public void onError(String e) {
                        }
                    }));
        });
    }

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("tokens.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void autoLogin(String email, String password) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().loginService(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", email)
                            .addFormDataPart("password", password)
                            .addFormDataPart("device_token", "")
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
                            loginSuccess(userResponse);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e, R.string.ok, null);
                        }
                    }));
        });
    }

    private void loginSuccess(UserResponse response) {
        getOptView().doIfPresent(view -> {
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putString(Constant.KEY_TOKEN, response.getApiToken());
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putInt(Constant.KEY_USER_ID, response.getId());

            String lang = !TextUtils.isEmpty(response.getLocale()) ? response.getLocale() : Locale.getDefault().getLanguage();
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putString(Constant.KEY_LANGUAGE, lang);
            LocaleHelper.setLocale(view.getAppActivity(), lang);

            view.showLoading(false);
            view.goToMain();
        });
    }
}
