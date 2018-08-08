package com.football.fantasy.fragments.more.profile.change;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ChangePasswordDataPresenter extends BaseDataPresenter<IChangePasswordView> implements IChangePasswordPresenter<IChangePasswordView> {
    /**
     * @param appComponent
     */
    protected ChangePasswordDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        getOptView().doIfPresent(v -> {
            RequestBody body = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("current_password", oldPassword)
                    .addFormDataPart("password", newPassword)
                    .addFormDataPart("password_confirmation", newPassword)
                    .build();

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().changePassword(body),
                    new ApiCallback<Object>() {
                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(Object response) {
                            v.changePasswordSuccessful();
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
