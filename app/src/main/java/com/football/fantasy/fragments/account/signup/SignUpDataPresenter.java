package com.football.fantasy.fragments.account.signup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.models.requests.SignupRequest;
import com.football.models.responses.UserResponse;
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
                            v.showMessage(R.string.register_successful, R.string.ok, null);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
