package com.football.fantasy.fragments.home;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class HomePresenter extends BaseDataPresenter<IHomeView> implements IHomePresenter<IHomeView> {
    /**
     * @param appComponent
     */
    protected HomePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
