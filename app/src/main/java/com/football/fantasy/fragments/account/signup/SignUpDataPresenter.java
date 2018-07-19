package com.football.fantasy.fragments.account.signup;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.requests.SignupRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

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
                                loginSuccess(response);
                            });
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    private void loginSuccess(UserResponse response) {
        getOptView().doIfPresent(view -> {
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putString(Constant.KEY_TOKEN, response.getApiToken());
            AppPreferences.getInstance(view.getAppActivity().getAppContext()).putInt(Constant.KEY_USER_ID, response.getId());
            view.goToMain();
            view.showLoading(false);
        });
    }
}
