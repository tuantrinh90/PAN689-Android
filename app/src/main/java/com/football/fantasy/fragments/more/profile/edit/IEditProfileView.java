package com.football.fantasy.fragments.more.profile.edit;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.UserResponse;

public interface IEditProfileView extends IBaseMvpView {
    void displayUser(UserResponse user);
}
