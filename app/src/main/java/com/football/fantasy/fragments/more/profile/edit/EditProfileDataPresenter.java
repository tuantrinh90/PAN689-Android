package com.football.fantasy.fragments.more.profile.edit;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.models.responses.UserResponse;
import com.football.utilities.Constant;

public class EditProfileDataPresenter extends BaseDataPresenter<IEditProfileView> implements IEditProfilePresenter<IEditProfileView> {
    /**
     * @param appComponent
     */
    protected EditProfileDataPresenter(AppComponent appComponent) {
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
