package com.football.fantasy.fragments.account.forgot;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class ForgotPasswordDataPresenter extends BaseDataPresenter<IForgotPasswordView> implements IForgotPasswordPresenter<IForgotPasswordView> {
    /**
     * @param appComponent
     */
    protected ForgotPasswordDataPresenter(AppComponent appComponent) {
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
                    v.onSuccess(email);
                }

                @Override
                public void onError(String error) {
                    v.showMessage(error);
                }
            }));
        });
    }
}
