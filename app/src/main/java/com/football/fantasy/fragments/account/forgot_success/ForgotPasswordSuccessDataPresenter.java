package com.football.fantasy.fragments.account.forgot_success;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
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
    public void recoverPassword(String email) {
        getOptView().doIfPresent(v -> {
            v.showLoading(true);
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().recoverPassword(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("email", email)
                            .build()),
                    new ApiCallback<Object>() {
                        @Override
                        public void onSuccess(Object response) {
                            v.showLoading(false);
                            v.showMessage("Re-send successful");
                            v.recoverSuccess();
                        }

                        @Override
                        public void onError(String e) {
                            v.showLoading(false);
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
