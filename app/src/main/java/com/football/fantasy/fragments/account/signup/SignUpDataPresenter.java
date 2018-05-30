package com.football.fantasy.fragments.account.signup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.requests.SignupRequest;
import com.football.models.responses.UserResponse;
import com.football.utilities.RxUtilities;

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
                    dataModule.getApiService().register(
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            request.getPasswordConfirm()),
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
            v.showLoading(false);
            v.goToMain();
        });
    }

    private void registerError(String e) {
        getOptView().doIfPresent(v -> {
            v.showMessage("Fake register nhé");
            v.showLoading(false);
            v.goToMain();
        });
    }
}
