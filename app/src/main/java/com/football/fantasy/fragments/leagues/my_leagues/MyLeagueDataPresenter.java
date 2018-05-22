package com.football.fantasy.fragments.leagues.my_leagues;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MyLeagueDataPresenter extends BaseDataPresenter<IMyLeagueView> implements IMyLeaguePresenter<IMyLeagueView> {
    /**
     * @param appComponent
     */
    public MyLeagueDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
