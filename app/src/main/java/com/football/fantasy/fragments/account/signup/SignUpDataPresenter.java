package com.football.fantasy.fragments.account.signup;

import com.bon.share_preferences.AppPreferences;
import com.football.application.AppContext;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
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
            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(
                    v,
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
                        public void onSuccess(UserResponse response) {
                            registerSuccess(response);
                        }

                        @Override
                        public void onError(String e) {
                            registerError(e);
                        }
                    }));
        });
    }

    private void registerSuccess(UserResponse response) {
        getOptView().doIfPresent(v -> {
            AppPreferences.getInstance(AppContext.getInstance()).putObject(Constant.KEY_USER, response);
            v.showLoading(false);
            v.goToMain();
        });
    }

    private void registerError(String e) {
        getOptView().doIfPresent(v -> {
            v.showMessage(e);
            v.showLoading(false);
        });
    }
}
