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
                            v.recoverSuccessful();
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
