package com.football.fantasy.fragments.more.profile.change;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class ChangePasswordDataPresenter extends BaseDataPresenter<IChangePasswordView> implements IChangePasswordPresenter<IChangePasswordView> {
    /**
     * @param appComponent
     */
    protected ChangePasswordDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
