package com.football.fantasy.fragments.home;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class HomeDataPresenter extends BaseDataPresenter<IHomeView> implements IHomePresenter<IHomeView> {
    /**
     * @param appComponent
     */
    protected HomeDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getMyLeagues() {

    }

    @Override
    public void getNews() {

    }
}
