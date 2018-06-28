package com.football.fantasy.fragments.more.profile;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;

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
            UserResponse user = AppPreferences.getInstance(v.getAppActivity()).getObject(Constant.KEY_USER, UserResponse.class);
            v.displayUser(user);

        });
    }
}
