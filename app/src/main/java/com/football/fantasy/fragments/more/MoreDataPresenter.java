package com.football.fantasy.fragments.more;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.helpers.sociallogin.facebook.FacebookHelper;
import com.football.helpers.sociallogin.google.GoogleHelper;
import com.football.listeners.ApiCallback;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;
import com.football.utilities.ServiceConfig;

import okhttp3.MultipartBody;

public class MoreDataPresenter extends BaseDataPresenter<IMoreView> implements IMorePresenter<IMoreView> {
    /**
     * @param appComponent
     */
    protected MoreDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void logout() {
        getOptView().doIfPresent(v -> {
            String provider = AppPreferences.getInstance(v.getAppActivity().getAppContext()).getString(Constant.KEY_LOGIN_TYPE);
            String deviceToken = AppPreferences.getInstance(v.getAppActivity().getAppContext()).getString(Constant.KEY_DEVICE_TOKEN);
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().logout(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("device_token", deviceToken)
                            .addFormDataPart("type", "1")
                            .build()),
                    new ApiCallback<Object>() {
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
                            switch (provider) {
                                case ServiceConfig.PROVIDER_FACEBOOK:
                                    FacebookHelper.quickSignOut();
                                    break;

                                case ServiceConfig.PROVIDER_GOOGLE:
                                    GoogleHelper.quickSignOut(v.getAppActivity());
                                    break;

                                case ServiceConfig.PROVIDER_TWITTER:
                                default:
                                    break;
                            }

                            AppPreferences.getInstance(v.getAppActivity()).clearCache();
                            v.logout();
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
