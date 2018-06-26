package com.football.fantasy.fragments.more.profile;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.UserResponse;

public interface IProfileView extends IBaseMvpView {
    void displayUser(UserResponse user);
}
