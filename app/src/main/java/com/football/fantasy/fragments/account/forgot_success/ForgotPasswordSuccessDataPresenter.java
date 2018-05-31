package com.football.fantasy.fragments.account.forgot_success;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.R;
import com.football.listeners.ApiCallback;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class ForgotPasswordSuccessDataPresenter extends BaseDataPresenter<IForgotPasswordSuccessView> implements IForgotPasswordSuccessPresenter<IForgotPasswordSuccessView> {
    /**
     * @param appComponent
     */
    protected ForgotPasswordSuccessDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void forgotPassword(String email) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().forgotPassword(new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email).build()), new ApiCallback<Object>() {
                @Override
                public void onStart() {
                    v.showLoading(true);
                }

                @Override
                public void onComplete() {
                    v.showLoading(false);
                }

                @Override
                public void onSuccess(Object o) {
                    v.showMessage(R.string.resend_forgot_success, R.string.ok, aVoid -> v.onSuccess());
                }

                @Override
                public void onError(String error) {
                    v.showMessage(error);
                }
            }));
        });
    }
}
