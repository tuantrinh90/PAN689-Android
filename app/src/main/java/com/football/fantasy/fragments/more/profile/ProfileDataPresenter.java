package com.football.fantasy.fragments.more.profile;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

public class ProfileDataPresenter extends BaseDataPresenter<IProfileView> implements IProfilePresenter<IProfileView> {
    /**
     * @param appComponent
     */
    protected ProfileDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getProfile() {
        getOptView().doIfPresent(v -> {
            int userId = AppPreferences.getInstance(v.getAppActivity()).getInt(Constant.KEY_USER_ID);
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getProfile(userId),
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
                            v.displayUser(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
