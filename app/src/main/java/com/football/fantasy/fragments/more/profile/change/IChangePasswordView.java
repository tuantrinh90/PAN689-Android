package com.football.fantasy.fragments.more.profile.change;

import com.football.common.views.IBaseMvpView;

public interface IChangePasswordView extends IBaseMvpView {
    void changePasswordSuccessful();

    void displayCurrentPasswordError();

}
